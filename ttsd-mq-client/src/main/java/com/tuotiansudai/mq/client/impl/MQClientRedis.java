package com.tuotiansudai.mq.client.impl;

import com.tuotiansudai.mq.client.MQClient;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.client.model.MessageTopic;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.function.Consumer;

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
    private boolean continueRunning = true;

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
        jedis.lpush(generateRedisKeyOfQueue(queue), message);
        logger.info("[MQ] push message to queue {} success, message: {}", queue.getQueueName(), message);
    }

    @Override
    public void subscribe(final MessageQueue queue, final Consumer<String> consumer) {
        logger.info("[MQ] subscribe queue: {}", queue.getQueueName());
        // use a new redis client for block request
        Jedis jedis = initJedis();
        while (continueRunning) {
            List<String> messages = jedis.brpop(10, generateRedisKeyOfQueue(queue));
            if (messages.size() == 0) {
                continue;
            }
            logger.info("[MQ] receive a message, prepare to consume");
            try {
                consumer.accept(messages.get(1));
            } catch (Exception e) {
                logger.error("[MQ] consume message failed", e);
                try {
                    jedis.rpush(generateRedisKeyOfQueue(queue), messages.get(1));
                    Thread.sleep(10 * 1000);
                } catch (InterruptedException ignored) {
                }
            }
            logger.info("[MQ] consume message success");
        }
        jedis.close();
    }

    @Override
    public void stopSubscribe() {
        logger.info("[MQ] prepare to stop");
        continueRunning = false;
    }

    private String generateRedisKeyOfQueue(MessageQueue queue) {
        return String.format("MQ:LOCAL:%s", queue.getQueueName());
    }

    private Jedis initJedis() {
        Jedis jedis = new Jedis(this.redisHost, this.redisPort);
        if (StringUtils.isNotBlank(redisPassword)) {
            jedis.auth(redisPassword);
        }
        jedis.connect();
        jedis.select(redisDB);
        return jedis;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.jedis = initJedis();
    }
}
