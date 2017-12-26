package com.tuotiansudai.mq.client.impl;

import com.aliyun.mns.client.CloudQueue;
import com.aliyun.mns.client.MNSClient;
import com.aliyun.mns.common.ClientException;
import com.aliyun.mns.common.ServiceException;
import com.aliyun.mns.model.Message;
import com.tuotiansudai.mq.client.MQConsumer;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.client.utils.AliyunMNSUtils;
import com.tuotiansudai.mq.config.setting.MessageConsumerSetting;
import com.tuotiansudai.mq.exception.AliyunClientException;
import com.tuotiansudai.mq.exception.AliyunServiceException;
import com.tuotiansudai.mq.tools.RedisClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class MQConsumerAliyunMNS extends MQConsumer {
    private static Logger logger = LoggerFactory.getLogger(MQConsumerAliyunMNS.class);

    private final MNSClient mnsClient;
    private final MessageConsumerSetting consumerSetting;
    private final AtomicInteger subscriberNum;

    public MQConsumerAliyunMNS(MNSClient mnsClient, RedisClient redisClient, MessageConsumerSetting consumerSetting) {
        super(redisClient.newJedis(), consumerSetting);
        this.mnsClient = mnsClient;
        this.consumerSetting = consumerSetting;
        this.subscriberNum = new AtomicInteger(0);
    }

    @Override
    public void subscribe(final MessageQueue queue, final Consumer<String> consumer) {
        logger.info("[MQ] subscribe queue: {}", queue.getQueueName());
        CloudQueue cloudQueue = AliyunMNSUtils.findQueue(mnsClient, queue);
        subscriberNum.incrementAndGet();
        int errorCount = 0;
        while (continueRunning) {
            boolean result = listenMessage(cloudQueue, queue.getQueueName(), consumer);
            // 报告消费者状态正常
            reportConsumerStatus(queue, result);
            // 异常则计数，正常则清零
            errorCount = result ? 0 : errorCount + 1;
            // 失败超过阈值则暂停订阅
            pauseSubscribeIfExceeds(queue, errorCount);
        }
        int subscriberNumber = subscriberNum.decrementAndGet();
        if (subscriberNumber == 0) {
            logger.info("[MQ] ready to stop");
            mnsClient.close();
        }
    }

    private boolean listenMessage(CloudQueue cloudQueue, String queueName, Consumer<String> consumer) {
        Message message = null;
        try {
            logger.info("[MQ] ready to pop message from queue: {}", queueName);
            message = cloudQueue.popMessage(consumerSetting.getMessagePopPeriodSeconds());
            if (message == null) {
                logger.info("[MQ] receive no message, try again");
            }
        } catch (ServiceException serviceException) {
            AliyunServiceException exp = new AliyunServiceException(serviceException);
            // 对于 ServiceException 类型的异常，仅记录异常，但不报告错误
            logger.error("[MQ] pop message fail", exp);
            // 稍微停一会儿
            try {
                Thread.sleep(consumerSetting.getMessagePopPeriodSeconds() * 1000);
            } catch (InterruptedException ignored) {
            }
        } catch (Exception e) {
            if (e instanceof ClientException) {
                e = new AliyunClientException((ClientException) e);
            }
            // 其它情况的异常需要报告错误
            logger.error("[MQ] pop message fail", e);
            return false;
        }
        if (message != null) {
            logger.info("[MQ] receive a message, ready to consume, queue: {}, messageId: {}", queueName, message.getMessageId());
            try {
                consumer.accept(message.getMessageBodyAsString());
                logger.info("[MQ] consume message success, queue: {}, messageId: {}", queueName, message.getMessageId());
                //删除已经取出消费的消息
                deleteMessage(cloudQueue, message);
            } catch (Exception e) {
                if (e instanceof ServiceException) {
                    e = new AliyunServiceException((ServiceException) e);
                } else if (e instanceof ClientException) {
                    e = new AliyunClientException((ClientException) e);
                }
                logger.error(String.format("[MQ] consume message fail, messageId: %s", message.getMessageId()), e);
            }
        }
        return true;
    }

    private void deleteMessage(CloudQueue cloudQueue, Message message) {
        int delete_try_count = 0;
        while (delete_try_count < 3) {
            try {
                cloudQueue.deleteMessage(message.getReceiptHandle());
                break;
            } catch (Exception e) {
                if (e instanceof ServiceException) {
                    e = new AliyunServiceException((ServiceException) e);
                } else if (e instanceof ClientException) {
                    e = new AliyunClientException((ClientException) e);
                }
                if (++delete_try_count < 3) {
                    logger.warn(String.format("[MQ] delete message fail [#%d], messageId: %s", delete_try_count, message.getMessageId()), e);
                } else {
                    logger.error(String.format("[MQ] delete message fail [#%d], messageId: %s", delete_try_count, message.getMessageId()), e);
                }
            }
        }
    }
}
