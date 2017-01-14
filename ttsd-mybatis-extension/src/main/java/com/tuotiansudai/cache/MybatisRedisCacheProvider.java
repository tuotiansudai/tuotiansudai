package com.tuotiansudai.cache;

import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.util.Objects;
import java.util.ResourceBundle;

public class MybatisRedisCacheProvider {
    private static final MybatisRedisCacheProvider instance = new MybatisRedisCacheProvider();
    private final static Logger logger = Logger.getLogger(MybatisRedisCacheProvider.class);

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
        int redisMaxWaitMills = Integer.parseInt(rb.getString("common.jedis.pool.maxWaitMillis"));

        if (Objects.equals(redisPassword, "")) {
            redisPassword = null;
        }

        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(redisPoolSize);
        config.setMaxIdle(5);
        config.setMaxWaitMillis(redisMaxWaitMills);
        config.setTestOnBorrow(true);
        this.jedisPool = new JedisPool(config, redisHostName, redisPort, Protocol.DEFAULT_TIMEOUT, redisPassword, redisDb);
    }

    public Jedis getResource() {
        int timeoutCount = 0;
        Jedis jedis;
        while (true) {
            try {
                jedis = jedisPool.getResource();
                break;
            } catch (JedisConnectionException e) {
                logger.warn(String.format("fetch jedis failed on %d times", timeoutCount + 1), e);
                if (++timeoutCount >= 3) {
                    logger.error("Get Redis pool failure more than 3 times.", e);
                    throw e;
                }
            }
        }
        return jedis;
    }

    int getExpireSeconds() {
        return expireSeconds;
    }
}
