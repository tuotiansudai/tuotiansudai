package com.tuotiansudai.mq.consumer.message.health;

import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.worker.monitor.HealthReporter;
import com.tuotiansudai.worker.monitor.JobWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

@Component
public class MessageConsumerHealthReporter extends HealthReporter {
    @Value("${common.redis.db}")
    private int redisDb;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Override
    protected JobWorker getCurrentJobWorker() {
        return JobWorker.MessageMQConsumer;
    }

    @Override
    protected Jedis getJedis() {
        Jedis jedis = new Jedis(redisWrapperClient.getRedisHost(), redisWrapperClient.getRedisPort());
        jedis.select(redisDb);
        return jedis;
    }
}
