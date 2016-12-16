package com.tuotiansudai.mq.client.impl;

import com.tuotiansudai.mq.client.MQProducer;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.client.model.MessageTopic;
import com.tuotiansudai.mq.tools.RedisClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

public class MQProducerRedis implements MQProducer {
    private static Logger logger = LoggerFactory.getLogger(MQProducerRedis.class);

    private final Jedis jedis;

    public MQProducerRedis(RedisClient redisClient) {
        this.jedis = redisClient.newJedis();
    }

    @Override
    public void publishMessage(final MessageTopic topic, final String message) {
        logger.info("[MQ] ready to publish message, topic: {}, message: {}", topic.getTopicName(), message);
        for (MessageQueue messageQueue : topic.getQueues()) {
            sendMessage(messageQueue, message);
        }
    }

    @Override
    public void sendMessage(final MessageQueue queue, final String message) {
        logger.info("[MQ] ready to send message, queue: {}, message: {}", queue.getQueueName(), message);
        synchronized (jedis) {
            jedis.lpush(generateRedisKeyOfQueue(queue), message);
        }
        logger.info("[MQ] push message to queue {} success, message: {}", queue.getQueueName(), message);
    }

    private String generateRedisKeyOfQueue(MessageQueue queue) {
        return String.format("MQ:LOCAL:%s", queue.getQueueName());
    }
}
