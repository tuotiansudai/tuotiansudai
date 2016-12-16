package com.tuotiansudai.mq.client.impl;

import com.aliyun.mns.client.CloudQueue;
import com.aliyun.mns.client.CloudTopic;
import com.aliyun.mns.client.MNSClient;
import com.tuotiansudai.mq.client.MQProducer;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.client.model.MessageTopic;
import com.tuotiansudai.mq.client.utils.AliyunMNSUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MQProducerAliyunMNS implements MQProducer {
    private static Logger logger = LoggerFactory.getLogger(MQProducerAliyunMNS.class);

    private final MNSClient mnsClient;

    public MQProducerAliyunMNS(MNSClient mnsClient) {
        this.mnsClient = mnsClient;
    }

    @Override
    public void publishMessage(final MessageTopic topic, final String message) {
        logger.info("[MQ] ready to publish message, topic: {}, message: '{}'", topic.getTopicName(), message);
        CloudTopic cloudTopic = AliyunMNSUtils.findTopic(mnsClient, topic);
        try {
            cloudTopic.publishMessage(AliyunMNSUtils.buildTopicMessage(message));
            logger.info("[MQ] publish message success, topic: {}, message: '{}'", topic.getTopicName(), message);
        } catch (Exception e) {
            logger.error("[MQ] publish message fail", e);
        }
    }

    @Override
    public void sendMessage(final MessageQueue queue, final String message) {
        logger.info("[MQ] ready to send message, queue: {}, message: '{}'", queue.getQueueName(), message);
        CloudQueue cloudQueue = AliyunMNSUtils.findQueue(mnsClient, queue);
        try {
            cloudQueue.putMessage(AliyunMNSUtils.buildQueueMessage(message));
            logger.info("[MQ] send message success, queue: {}, message: '{}'", queue.getQueueName(), message);
        } catch (Exception e) {
            logger.error("[MQ] send message fail", e);
        }
    }

}
