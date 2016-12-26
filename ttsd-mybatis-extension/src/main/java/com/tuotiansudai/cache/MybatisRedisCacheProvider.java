package com.tuotiansudai.cache;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

import java.util.Objects;
import java.util.ResourceBundle;

public class MybatisRedisCacheProvider {
    private static final MybatisRedisCacheProvider instance = new MybatisRedisCacheProvider();

    private final JedisPool jedisPool;
    private final int expireSeconds;

    public static MybatisRedisCacheProvider getInstance() {
        return instance;
    }

    private MybatisRedisCacheProvider() {
        ResourceBundle rb = ResourceBundle.getBundle("ttsd-env");
        this.expireSeconds = Integer.parseInt(rb.getString("common.mybatis.cache.expire.seconds"));
        String redisHostName = rb.getString("common.redis.host");
        int redisPort = Integer.parseInt(rb.getString("common.redis.port"));
        String redisPassword = rb.getString("common.redis.password");
        int redisDb = Integer.parseInt(rb.getString("common.mybatis.cache.db"));
        int redisPoolSize = Integer.parseInt(rb.getString("common.mybatis.cache.redis.pool.maxTotal"));

        if (Objects.equals(redisPassword, "")) {
            redisPassword = null;
        }

        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(redisPoolSize);
        config.setMaxIdle(5);
        config.setMaxWaitMillis(1000 * 60);
        config.setTestOnBorrow(true);
        this.jedisPool = new JedisPool(config, redisHostName, redisPort, Protocol.DEFAULT_TIMEOUT, redisPassword, redisDb);
    }

    public Jedis getResource() {
        return jedisPool.getResource();
    }

    int getExpireSeconds() {
        return expireSeconds;
    }
}
