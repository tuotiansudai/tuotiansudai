package com.tuotiansudai.client;

import com.google.common.base.Strings;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.text.MessageFormat;
import java.util.Map;

@Component
public class SiteMapRedisWrapperClient {

    static Logger logger = Logger.getLogger(SiteMapRedisWrapperClient.class);

    @Value("${cms.redis.db}")
    private int sitemapRedisDb;

    @Value("${cms.redis.host}")
    private String redisHost;

    @Value("${cms.redis.port}")
    private int redisPort;

    @Value("${cms.redis.password}")
    private String redisPassword;

    @Value("${mobile.jedis.pool.maxTotal}")
    private int maxTotal;

    @Value("${cms.jedis.pool.maxWaitMillis}")
    private int maxWaitMillis;

    private static JedisPool jedisPool;

    private <T> T execute(JedisAction<T> jedisAction) {
        Jedis jedis = null;
        try {
            jedis = getJedis(this.sitemapRedisDb);
            return jedisAction.action(jedis);
        } finally {
            closeResource(jedis);
        }
    }

    public boolean exists(final String key) {
        return execute(new JedisAction<Boolean>() {
            @Override
            public Boolean action(Jedis jedis) {
                return jedis.exists(key);
            }
        });
    }

    public Long hset(final String key, final String hkey, final String value) {
        return execute(new JedisAction<Long>() {
            @Override
            public Long action(Jedis jedis) {
                return jedis.hset(key, hkey, value);
            }
        });
    }

    public Long hset(final String key, final String hkey, final String value, final int lifeSecond) {
        return execute(new JedisAction<Long>() {
            @Override
            public Long action(Jedis jedis) {
                jedis.expire(key, lifeSecond);
                return jedis.hset(key, hkey, value);
            }
        });
    }

    public String hget(final String key, final String hkey) {
        return execute(new JedisAction<String>() {
            @Override
            public String action(Jedis jedis) {
                return jedis.hget(key, hkey);
            }
        });
    }

    public Map<String, String> hgetAll(final String key) {
        return execute(new JedisAction<Map<String, String>>() {
            @Override
            public Map<String, String> action(Jedis jedis) {
                return jedis.hgetAll(key);
            }
        });
    }

    public boolean hexists(final String key, final String field) {
        return execute(new JedisAction<Boolean>() {
            @Override
            public Boolean action(Jedis jedis) {
                return jedis.hexists(key, field);
            }
        });
    }

    protected Jedis getJedis(int db) {
        int timeoutCount = 0;
        Jedis jedis;
        while (true) {
            try {
                jedis = getJedisPool().getResource();
                if (!Strings.isNullOrEmpty(getRedisPassword())) {
                    jedis.auth(getRedisPassword());
                }
                jedis.select(db);
                break;
            } catch (JedisConnectionException e) {
                logger.warn(MessageFormat.format("fetch jedis failed on {0} times", String.valueOf(timeoutCount + 1)), e);
                if (++timeoutCount >= 3) {
                    logger.error("Get Redis pool failure more than 3 times.", e);
                    throw e;
                }
            }
        }
        return jedis;
    }

    protected JedisPool getJedisPool() {
        if (jedisPool == null) {
            JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
            jedisPoolConfig.setMaxTotal(maxTotal);
            jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
            logger.info("redisHost=" + redisHost);
            logger.info("redisPort=" + redisPort);
            logger.info("maxTotal=" + jedisPoolConfig.getMaxTotal());
            logger.info("MaxWaitMillis=" + jedisPoolConfig.getMaxWaitMillis());

            jedisPool = new JedisPool(jedisPoolConfig, redisHost, redisPort);
        }
        return jedisPool;
    }

    public interface JedisAction<T> {
        T action(Jedis jedis);
    }

    protected void closeResource(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }

    public String getRedisHost() {
        return redisHost;
    }

    public void setRedisHost(String redisHost) {
        this.redisHost = redisHost;
    }

    public int getRedisPort() {
        return redisPort;
    }

    public String getRedisPassword() {
        return redisPassword;
    }

    public void setRedisPassword(String redisPassword) {
        this.redisPassword = redisPassword;
    }

    public void setRedisPort(int redisPort) {
        this.redisPort = redisPort;
    }
}