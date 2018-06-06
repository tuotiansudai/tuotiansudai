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

    private final static int maxTotal = 300;

    private final static int maxWaitMillis = 5000;

    private static JedisPool jedisPool;

    private <T> T execute(JedisAction<T> jedisAction) {
        try (Jedis jedis = getJedis(this.sitemapRedisDb)) {
            if (jedis != null) {
                return jedisAction.action(jedis);
            }
        } catch (Exception ex) {
            logger.error(ex.getLocalizedMessage(), ex);
        }

        return null;
    }

    public boolean exists(final String key) {
        Boolean isExisted = execute(jedis -> jedis.exists(key));
        return isExisted != null && isExisted;
    }

    public Long hset(final String key, final String hkey, final String value) {
        return execute(jedis -> jedis.hset(key, hkey, value));
    }

    public Long hset(final String key, final String hkey, final String value, final int lifeSecond) {
        return execute(jedis -> {
            jedis.expire(key, lifeSecond);
            return jedis.hset(key, hkey, value);
        });
    }

    public String hget(final String key, final String hkey) {
        return execute(jedis -> jedis.hget(key, hkey));
    }

    public Map<String, String> hgetAll(final String key) {
        return execute(jedis -> jedis.hgetAll(key));
    }

    public boolean hexists(final String key, final String field) {
        Boolean isExisted = execute(jedis -> jedis.hexists(key, field));

        return isExisted != null && isExisted;
    }

    public String get(final String key) {
        return execute(jedis -> jedis.get(key));
    }

    public String set(final String key, final String value) {
        return execute(jedis -> jedis.set(key, value));
    }

    protected Jedis getJedis(int db) {
        int timeoutCount = 0;
        while (true) {
            try {
                Jedis jedis = getJedisPool().getResource();
                if (!Strings.isNullOrEmpty(getRedisPassword())) {
                    jedis.auth(getRedisPassword());
                }
                jedis.select(db);
                break;
            } catch (JedisConnectionException e) {
                logger.warn(MessageFormat.format("fetch jedis failed on {0} times", String.valueOf(timeoutCount + 1)), e);
                if (++timeoutCount >= 3) {
                    logger.error("Get Redis pool failure more than 3 times.", e);
                    break;
                }
            }
        }
        return null;
    }

    public boolean del(final String... keys) {
        Boolean isDeleted = execute(jedis -> jedis.del(keys) == keys.length);
        return isDeleted != null && isDeleted;
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