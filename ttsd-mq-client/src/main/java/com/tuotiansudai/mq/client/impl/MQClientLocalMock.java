package com.tuotiansudai.mq.client.impl;

import com.tuotiansudai.mq.client.MQClient;
import com.tuotiansudai.mq.client.model.Message;
import com.tuotiansudai.mq.client.model.MessageTopic;
import com.tuotiansudai.mq.client.model.MessageTopicQueue;
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

public class MQClientLocalMock implements MQClient, InitializingBean {

    @Value("${common.redis.host}")
    private String redisHost;
    @Value("${common.redis.port}")
    private int redisPort;
    @Value("${common.redis.password}")
    private String redisPassword;
    @Value("${common.redis.db}")
    private int redisDB;

    private static Logger logger = LoggerFactory.getLogger(MQClientLocalMock.class);
    private Jedis jedis;

    public void publishMessage(final MessageTopic topic, final String message) {
        logger.info("[MQ] ready to push message, topic: {}, message: {}", topic, message);
        Stream.of(MessageTopicQueue.values())
                .filter(q -> q.getTopic().equals(topic))
                .forEach(q -> sendMessage(q, message));
    }

    public void subscribe(final MessageTopicQueue queue, Consumer<com.tuotiansudai.mq.client.model.Message> consumer) {
        logger.info("[MQ] subscribe topic: {}, queue: {}", queue.getTopic(), queue.getQueueName());
        while (true) {
            List<String> messages = jedis.blpop(0, generateRedisKeyOfQueue(queue));
            logger.debug("[MQ] receive a message, prepare to consume");
            consumer.accept(new Message(String.valueOf(Clock.systemUTC().millis()), messages.get(1), "", queue.getTopic().getTopicName()));
            logger.info("[MQ] consume message success");
        }
    }

    private void sendMessage(MessageTopicQueue queue, String message) {
        jedis.lpush(generateRedisKeyOfQueue(queue), message);
        logger.info("[MQ] push message to queue {} success, message: {}", queue.getQueueName(), message);
    }

    private String generateRedisKeyOfQueue(MessageTopicQueue queue) {
        return String.format("MQ:LOCAL:%s:%s", queue.getTopic().getTopicName(), queue.getQueueName());
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
