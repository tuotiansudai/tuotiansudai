package com.tuotiansudai.mq.client.utils;

import com.aliyun.mns.client.CloudQueue;
import com.aliyun.mns.client.CloudTopic;
import com.aliyun.mns.client.MNSClient;
import com.aliyun.mns.common.ServiceException;
import com.aliyun.mns.model.Base64TopicMessage;
import com.aliyun.mns.model.Message;
import com.aliyun.mns.model.TopicMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.client.model.MessageTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class AliyunMNSUtils {
    private static Logger logger = LoggerFactory.getLogger(AliyunMNSUtils.class);

    private static final Map<MessageTopic, CloudTopic> topicMap = new HashMap<>();
    private static final Map<MessageQueue, CloudQueue> queueMap = new HashMap<>();

    public static CloudQueue findQueue(MNSClient mnsClient, MessageQueue queue) {
        if (queueMap.containsKey(queue)) {
            return queueMap.get(queue);
        }
        try {
            CloudQueue cloudQueue = mnsClient.getQueueRef(queue.getQueueName());
            queueMap.put(queue, cloudQueue);
            return cloudQueue;
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

    public static CloudTopic findTopic(MNSClient mnsClient, MessageTopic topic) {
        if (topicMap.containsKey(topic)) {
            return topicMap.get(topic);
        }
        try {
            CloudTopic cloudTopic = mnsClient.getTopicRef(topic.getTopicName());
            topicMap.put(topic, cloudTopic);
            return cloudTopic;
        } catch (Exception e) {
            logger.error("[MQ] find topic fail", e);
            throw e;
        }
    }

    public static TopicMessage buildTopicMessage(String message) {
        TopicMessage msg = new Base64TopicMessage();
        msg.setMessageBody(message);
        return msg;
    }

    public static Message buildQueueMessage(String message) {
        return new Message(message);
    }
}
