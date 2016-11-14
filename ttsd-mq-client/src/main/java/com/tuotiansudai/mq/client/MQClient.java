package com.tuotiansudai.mq.client;

import com.aliyun.mns.client.CloudQueue;
import com.aliyun.mns.client.CloudTopic;
import com.aliyun.mns.client.MNSClient;
import com.aliyun.mns.common.ServiceException;
import com.aliyun.mns.model.Message;
import com.aliyun.mns.model.RawTopicMessage;
import com.aliyun.mns.model.TopicMessage;
import com.google.gson.Gson;
import com.tuotiansudai.mq.client.model.MessageTopic;
import com.tuotiansudai.mq.client.model.MessageTopicQueue;
import com.tuotiansudai.mq.client.support.RawMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class MQClient {

    private static Logger logger = LoggerFactory.getLogger(MQClient.class);

    private final MNSClient mnsClient;
    private final Map<MessageTopic, CloudTopic> topicMap;
    private final Gson gson;

    public MQClient(MNSClient mnsClient) {
        this.mnsClient = mnsClient;
        this.topicMap = new HashMap<>();
        this.gson = new Gson();
    }

    public void publishMessage(final MessageTopic topic, final String message) {
        logger.info("[MQ] ready to push message, topic: {}, message: {}", topic, message);
        CloudTopic cloudTopic = findTopic(topic);
        try {
            cloudTopic.publishMessage(buildRawMessage(message));
            logger.info("[MQ] push message success, topic: {}, message: {}", topic, message);
        } catch (Exception e) {
            logger.error("[MQ] publish message fail", e);
        }
    }

    public void subscribe(final MessageTopicQueue queue, Consumer<com.tuotiansudai.mq.client.model.Message> consumer) {
        logger.info("[MQ] subscribe topic: {}, queue: {}", queue.getTopic(), queue.getQueueName());
        CloudQueue cloudQueue = findQueue(queue);
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
                    RawMessage rawMessage = gson.fromJson(message.getMessageBodyAsRawString(), RawMessage.class);
                    consumer.accept(rawMessage.toMessage());
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

    private CloudQueue findQueue(MessageTopicQueue queue) {
        try {
            return mnsClient.getQueueRef(queue.getQueueName());
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
