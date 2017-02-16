package com.tuotiansudai.mq.client.impl;

import com.tuotiansudai.mq.client.MQConsumer;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.config.setting.MessageConsumerSetting;
import com.tuotiansudai.mq.tools.RedisClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.function.Consumer;

public class MQConsumerRedis extends MQConsumer {
    private static Logger logger = LoggerFactory.getLogger(MQConsumerRedis.class);

    private final RedisClient redisClient;
    private final MessageConsumerSetting consumerSetting;

    public MQConsumerRedis(RedisClient redisClient, MessageConsumerSetting consumerSetting) {
        super(redisClient.newJedis(), consumerSetting);
        this.redisClient = redisClient;
        this.consumerSetting = consumerSetting;
    }

    @Override
    public void subscribe(final MessageQueue queue, final Consumer<String> consumer) {
        logger.info("[MQ] subscribe queue: {}", queue.getQueueName());
        // use a new redis client for block request
        Jedis jedis = redisClient.newJedis();
        int errorCount = 0;
        while (continueRunning) {
            boolean result = listenMessage(queue, consumer, jedis);
            // 报告消费者状态正常
            reportConsumerStatus(queue, result);
            // 异常则计数，正常则清零
            errorCount = result ? 0 : errorCount + 1;
            // 失败超过阈值则暂停订阅
//            pauseSubscribeIfExceeds(queue, errorCount);
        }
        jedis.close();
    }

    private boolean listenMessage(MessageQueue queue, Consumer<String> consumer, Jedis jedis) {
        List<String> messages = jedis.brpop(consumerSetting.getMessagePopPeriodSeconds(), generateRedisKeyOfQueue(queue));
        if (messages.size() == 0) {
            return true;
        }
        logger.info("[MQ] receive a message, prepare to consume");
        try {
            consumer.accept(messages.get(1));
        } catch (Exception e) {
            logger.error("[MQ] consume message failed", e);
            jedis.rpush(generateRedisKeyOfQueue(queue), messages.get(1));
            return false;
        }
        logger.info("[MQ] consume message success");
        return true;
    }

    private String generateRedisKeyOfQueue(MessageQueue queue) {
        return String.format("MQ:LOCAL:%s", queue.getQueueName());
    }
}
