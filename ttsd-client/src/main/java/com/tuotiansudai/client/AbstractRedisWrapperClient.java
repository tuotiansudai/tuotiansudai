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
                logger.error(MessageFormat.format("fetch jedis failed on {0} times", String.valueOf(timeoutCount + 1)), e);
                if (++timeoutCount >= 3) {
                    logger.error("Get Redis pool failure more than 3 times.",e);
                    throw e;
                }
            }
        }
        return jedis;
    }

    protected JedisPool getJedisPool() {
        if (jedisPool == null) {
            logger.debug("redisHost=" + redisHost);
            logger.debug("redisPort=" + redisPort);
            logger.debug("maxTotal=" + jedisPoolConfig.getMaxTotal());
            logger.debug("maxTotal=" + jedisPoolConfig.getMaxWaitMillis());
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
