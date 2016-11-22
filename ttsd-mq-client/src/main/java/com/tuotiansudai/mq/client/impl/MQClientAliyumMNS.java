package com.tuotiansudai.mq.client.impl;

import com.aliyun.mns.client.CloudQueue;
import com.aliyun.mns.client.CloudTopic;
import com.aliyun.mns.client.MNSClient;
import com.aliyun.mns.common.ServiceException;
import com.aliyun.mns.model.Message;
import com.aliyun.mns.model.RawTopicMessage;
import com.aliyun.mns.model.TopicMessage;
import com.google.gson.Gson;
import com.tuotiansudai.mq.client.MQClient;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.client.model.MessageTopic;
import com.tuotiansudai.mq.client.model.MessageTopicQueue;
import com.tuotiansudai.mq.client.model.Queue;
import com.tuotiansudai.mq.client.support.RawMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class MQClientAliyumMNS implements MQClient {

    private static Logger logger = LoggerFactory.getLogger(MQClientAliyumMNS.class);

    private final MNSClient mnsClient;
    private final Map<MessageTopic, CloudTopic> topicMap;
    private final Gson gson;

    public MQClientAliyumMNS(MNSClient mnsClient) {
        this.mnsClient = mnsClient;
        this.topicMap = new HashMap<>();
        this.gson = new Gson();
    }

    @Override
    public void publishMessage(final MessageTopic topic, final String message) {
        logger.info("[MQ] ready to publish message, topic: {}, message: '{}'", topic.getTopicName(), message);
        CloudTopic cloudTopic = findTopic(topic);
        try {
            cloudTopic.publishMessage(buildRawMessage(message));
            logger.info("[MQ] publish message success, topic: {}, message: '{}'", topic.getTopicName(), message);
        } catch (Exception e) {
            logger.error("[MQ] publish message fail", e);
        }
    }

    @Override
    public void sendMessage(MessageQueue queue, String message) {
        logger.info("[MQ] ready to send message, queue: {}, message: '{}'", queue.getQueueName(), message);
        CloudQueue cloudQueue = findQueue(queue.getQueueName());
        try {
            cloudQueue.putMessage(new Message(message));
            logger.info("[MQ] send message success, queue: {}, message: '{}'", queue.getQueueName(), message);
        } catch (Exception e) {
            logger.error("[MQ] send message fail", e);
        }
    }

    @Override
    public void subscribe(final Queue queue, Consumer<com.tuotiansudai.mq.client.model.Message> consumer) {
        logger.info("[MQ] subscribe queue: {}", queue.getQueueName());
        CloudQueue cloudQueue = findQueue(queue.getQueueName());
        while (true) {
            Message message = null;
            try {
                logger.debug("[MQ] ready to pop message from queue: {}", queue.getQueueName());
                message = cloudQueue.popMessage(30);
                if (message == null) {
                    logger.debug("[MQ] receive no message, try again");
                } else {
                    logger.debug("[MQ] receive a message, prepare to consume");
                }
            } catch (Exception e) {
                logger.error("[MQ] pop message fail", e);
            }
            if (message != null) {
                logger.info("[MQ] ready to consume message, queue: {}, messageId: {}",
                        queue.getQueueName(), message.getMessageId());
                try {
                    if (queue instanceof MessageQueue) {
                        consumer.accept(parseQueueMessage(message));
                    }
                    if (queue instanceof MessageTopicQueue) {
                        consumer.accept(parseTopicQueueMessage(message));
                    }
                    logger.info("[MQ] consume message success, queue: {}, messageId: {}",
                            queue.getQueueName(), message.getMessageId());
                    try {
                        //删除已经取出消费的消息
                        cloudQueue.deleteMessage(message.getReceiptHandle());
                    } catch (Exception e) {
                        logger.error(String.format("[MQ] delete message fail, messageId: %s", message.getMessageId()), e);
                    }
                } catch (Exception e) {
                    logger.error(String.format("[MQ] consume message fail, messageId: %s", message.getMessageId()), e);
                }
            }
        }
    }

    private com.tuotiansudai.mq.client.model.Message parseTopicQueueMessage(Message message) {
        RawMessage rawMessage = gson.fromJson(message.getMessageBodyAsRawString(), RawMessage.class);
        return rawMessage.toMessage();
    }

    private com.tuotiansudai.mq.client.model.Message parseQueueMessage(Message message) {
        return new com.tuotiansudai.mq.client.model.Message(message.getMessageId(),
                message.getMessageBodyAsString(), message.getEnqueueTime().toString(), "");
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

    private TopicMessage buildRawMessage(String message) {
        TopicMessage msg = new RawTopicMessage();
        msg.setMessageBody(message);
        return msg;
    }
}
