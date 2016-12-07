package com.tuotiansudai.mq.client.impl;

import com.aliyun.mns.client.CloudQueue;
import com.aliyun.mns.client.CloudTopic;
import com.aliyun.mns.client.MNSClient;
import com.aliyun.mns.common.ServiceException;
import com.aliyun.mns.model.Base64TopicMessage;
import com.aliyun.mns.model.Message;
import com.aliyun.mns.model.TopicMessage;
import com.tuotiansudai.mq.client.MQClient;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.client.model.MessageTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class MQClientAliyunMNS implements MQClient {

    private static Logger logger = LoggerFactory.getLogger(MQClientAliyunMNS.class);

    private final MNSClient mnsClient;
    private final Map<MessageTopic, CloudTopic> topicMap;
    private boolean continueRunning = true;
    private AtomicInteger subscriberNum = new AtomicInteger(0);

    public MQClientAliyunMNS(MNSClient mnsClient) {
        this.mnsClient = mnsClient;
        this.topicMap = new HashMap<>();
    }

    @Override
    public void publishMessage(final MessageTopic topic, final String message) {
        logger.info("[MQ] ready to publish message, topic: {}, message: '{}'", topic.getTopicName(), message);
        CloudTopic cloudTopic = findTopic(topic);
        try {
            cloudTopic.publishMessage(buildTopicMessage(message));
            logger.info("[MQ] publish message success, topic: {}, message: '{}'", topic.getTopicName(), message);
        } catch (Exception e) {
            logger.error("[MQ] publish message fail", e);
        }
    }

    @Override
    public void sendMessage(final MessageQueue queue, final String message) {
        logger.info("[MQ] ready to send message, queue: {}, message: '{}'", queue.getQueueName(), message);
        CloudQueue cloudQueue = findQueue(queue.getQueueName());
        try {
            cloudQueue.putMessage(buildQueueMessage(message));
            logger.info("[MQ] send message success, queue: {}, message: '{}'", queue.getQueueName(), message);
        } catch (Exception e) {
            logger.error("[MQ] send message fail", e);
        }
    }

    @Override
    public void subscribe(final MessageQueue queue, final Consumer<String> consumer) {
        logger.info("[MQ] subscribe queue: {}", queue.getQueueName());
        CloudQueue cloudQueue = findQueue(queue.getQueueName());
        subscriberNum.incrementAndGet();
        int errorCount = 0;
        while (continueRunning) {
            boolean result = listenMessage(cloudQueue, queue.getQueueName(), consumer);
            if (result) {
                errorCount = 0;
            } else {
                errorCount++;
            }
            if (errorCount >= 10) {
                sleepForCircles(6 * 30);// half an hour
            }
        }
        int subscriberNumber = subscriberNum.decrementAndGet();
        if (subscriberNumber == 0) {
            logger.info("[MQ] ready to stop");
            mnsClient.close();
        }
    }

    private void sleepForCircles(int circles) {
        try {
            while (continueRunning && ((circles--) > 0)) {
                Thread.sleep(TIME_SLICE_SECONDS * 1000L);
            }
        } catch (InterruptedException ignored) {
        }
    }

    private boolean listenMessage(CloudQueue cloudQueue, String queueName, Consumer<String> consumer) {
        Message message = null;
        try {
            logger.debug("[MQ] ready to pop message from queue: {}", queueName);
            message = cloudQueue.popMessage(TIME_SLICE_SECONDS);
            if (message == null) {
                logger.debug("[MQ] receive no message, try again");
            } else {
                logger.debug("[MQ] receive a message, prepare to consume");
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

    @Override
    public void stopSubscribe() {
        logger.info("[MQ] prepare to stop");
        continueRunning = false;
    }

    private CloudTopic findTopic(MessageTopic topic) {
        if (topicMap.containsKey(topic)) {
            return topicMap.get(topic);
        }
        try {
            CloudTopic cloudTopic = mnsClient.getTopicRef(topic.name());
            topicMap.put(topic, cloudTopic);
            return cloudTopic;
        } catch (Exception e) {
            logger.error("[MQ] find topic fail", e);
            throw e;
        }
    }

    private CloudQueue findQueue(String queueName) {
        try {
            return mnsClient.getQueueRef(queueName);
        } catch (ServiceException se) {
            if (se.getErrorCode() != null) {
                if (se.getErrorCode().equals("QueueNotExist")) {
                    logger.error("[MQ] find queue fail, queue is not exist", se);
                } else if (se.getErrorCode().equals("TimeExpired")) {
                    logger.error("[MQ] find queue fail, request time expired. Please check your local time", se);
                }
            } else {
                logger.error("[MQ] find queue fail", se);
            }
            throw se;
        } catch (Exception e) {
            logger.error("[MQ] find queue fail", e);
            throw e;
        }
    }

    private TopicMessage buildTopicMessage(String message) {
        TopicMessage msg = new Base64TopicMessage();
        msg.setMessageBody(message);
        return msg;
    }

    private Message buildQueueMessage(String message) {
        return new Message(message);
    }
}
