package com.tuotiansudai.mq.client.impl;

import com.aliyun.mns.client.CloudQueue;
import com.aliyun.mns.client.MNSClient;
import com.aliyun.mns.model.Message;
import com.tuotiansudai.mq.client.MQConsumer;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.client.utils.AliyunMNSUtils;
import com.tuotiansudai.mq.config.setting.MessageConsumerSetting;
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
            } else {
                logger.info("[MQ] receive a message, prepare to consume");
            }
        } catch (Exception e) {
            logger.error("[MQ] pop message fail", e);
            return false;
        }
        if (message != null) {
            logger.info("[MQ] ready to consume message, queue: {}, messageId: {}", queueName, message.getMessageId());
            try {
                consumer.accept(message.getMessageBodyAsString());
                logger.info("[MQ] consume message success, queue: {}, messageId: {}", queueName, message.getMessageId());
                try {
                    //删除已经取出消费的消息
                    cloudQueue.deleteMessage(message.getReceiptHandle());
                } catch (Exception e) {
                    logger.error(String.format("[MQ] delete message fail, messageId: %s", message.getMessageId()), e);
                }
            } catch (Exception e) {
                logger.error(String.format("[MQ] consume message fail, messageId: %s", message.getMessageId()), e);
                return false;
            }
        }
        return true;
    }
}
