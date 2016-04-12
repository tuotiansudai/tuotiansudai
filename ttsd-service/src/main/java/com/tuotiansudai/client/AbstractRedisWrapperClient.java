package com.tuotiansudai.client;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisDataException;
import redis.clients.jedis.exceptions.JedisException;

@Component
public abstract class AbstractRedisWrapperClient {

    private static Logger logger = Logger.getLogger(AbstractRedisWrapperClient.class);

    @Value("${common.redis.host}")
    private String redisHost;

    @Value("${common.redis.port}")
    private int redisPort;

    @Value("${common.redis.password}")
    private String redisPassword;

    @Autowired
    private JedisPoolConfig jedisPoolConfig;

    private static JedisPool jedisPool;

    protected JedisPool getJedisPool() {
        if (jedisPool == null) {
            jedisPool = new JedisPool(jedisPoolConfig, redisHost, redisPort);
        }
        return jedisPool;
    }

    public interface JedisAction<T> {
        T action(Jedis jedis);
    }

    public interface JedisActionNoResult {
        void action(Jedis jedis);
    }

    protected boolean handleJedisException(JedisException jedisException) {
        if (jedisException instanceof JedisConnectionException) {
            logger.error(jedisException.getLocalizedMessage(), jedisException);
        } else if (jedisException instanceof JedisDataException) {
            if ((jedisException.getMessage() != null) && (jedisException.getMessage().contains("READONLY"))) {
                logger.error(jedisException.getLocalizedMessage(), jedisException);
            } else {
                return false;
            }
        } else {
            logger.error(jedisException.getLocalizedMessage(), jedisException);
        }
        return true;
    }

    protected void closeResource(Jedis jedis, boolean connectionBroken) {
        try {
            if (connectionBroken) {
                jedisPool.returnBrokenResource(jedis);
            } else {
                jedisPool.returnResource(jedis);
            }
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            destroyJedis(jedis);
        }
    }

    private static void destroyJedis(Jedis jedis) {
        if ((jedis != null) && jedis.isConnected()) {
            try {
                try {
                    jedis.quit();
                } catch (Exception e) {
                    logger.error(e.getLocalizedMessage(), e);
                }
                jedis.disconnect();
            } catch (Exception e) {
                logger.error(e.getLocalizedMessage(), e);
            }
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
