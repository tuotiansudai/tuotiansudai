package com.tuotiansudai.mq.client.impl;

import com.tuotiansudai.mq.client.MQClient;
import com.tuotiansudai.mq.client.model.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import redis.clients.jedis.Jedis;

import java.time.Clock;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class MQClientRedis implements MQClient, InitializingBean {

    @Value("${common.redis.host}")
    private String redisHost;
    @Value("${common.redis.port}")
    private int redisPort;
    @Value("${common.redis.password}")
    private String redisPassword;
    @Value("${common.redis.db}")
    private int redisDB;

    private static Logger logger = LoggerFactory.getLogger(MQClientRedis.class);
    private Jedis jedis;

    @Override
    public void publishMessage(final MessageTopic topic, final String message) {
        logger.info("[MQ] ready to publish message, topic: {}, message: {}", topic.getTopicName(), message);
        Stream.of(MessageTopicQueue.values())
                .filter(q -> q.getTopic().equals(topic))
                .forEach(q -> sendMessage(q, message));
    }

    @Override
    public void sendMessage(MessageQueue queue, String message) {
        logger.info("[MQ] ready to send message, queue: {}, message: {}", queue.getQueueName(), message);
        jedis.lpush(generateRedisKeyOfQueue(queue), message);
        logger.info("[MQ] push message to queue {} success, message: {}", queue.getQueueName(), message);
    }

    @Override
    public void subscribe(final Queue queue, Consumer<com.tuotiansudai.mq.client.model.Message> consumer) {
        logger.info("[MQ] subscribe queue: {}", queue.getQueueName());
        while (true) {
            List<String> messages = jedis.brpop(0, generateRedisKeyOfQueue(queue));
            logger.debug("[MQ] receive a message, prepare to consume");
            consumer.accept(new Message(String.valueOf(Clock.systemUTC().millis()), messages.get(1), "", ""));
            logger.info("[MQ] consume message success");
        }
    }

    private void sendMessage(MessageTopicQueue queue, String message) {
        logger.info("[MQ] ready to send message, queue: {}, message: {}", queue.getQueueName(), message);
        jedis.lpush(generateRedisKeyOfQueue(queue), message);
        logger.info("[MQ] push message to queue {} success, message: {}", queue.getQueueName(), message);
    }

    private String generateRedisKeyOfQueue(Queue queue) {
        String topicName = (queue instanceof MessageTopicQueue) ? ((MessageTopicQueue) queue).getTopic().getTopicName() : "-";
        return String.format("MQ:LOCAL:%s:%s", topicName, queue.getQueueName());
    }

    private void initJedis() {
        Jedis jedis = new Jedis(this.redisHost, this.redisPort);
        if (StringUtils.isNotBlank(redisPassword)) {
            jedis.auth(redisPassword);
        }
        jedis.connect();
        jedis.select(redisDB);
        this.jedis = jedis;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initJedis();
    }
}
