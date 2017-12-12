package com.tuotiansudai.util;

import com.google.common.base.Strings;
import com.tuotiansudai.etcd.ETCDConfigReader;
import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.text.MessageFormat;
import java.util.ResourceBundle;

public abstract class AbstractRedisWrapperClient {

    private static Logger logger = Logger.getLogger(AbstractRedisWrapperClient.class);

    private static String REDIS_HOST;

    private static int REDIS_PORT;

    private static String REDIS_PASSWORD;

    private static int MAX_WAIT_MILLIS;

    private static JedisPool JEDIS_POOL;

    static {
        ETCDConfigReader etcdConfigReader = ETCDConfigReader.getReader();
        REDIS_HOST = etcdConfigReader.getValue("common.redis.host");
        REDIS_PORT = Integer.parseInt(etcdConfigReader.getValue("common.redis.port"));
        REDIS_PASSWORD = etcdConfigReader.getValue("common.redis.password");
        MAX_WAIT_MILLIS = Integer.parseInt(etcdConfigReader.getValue("common.jedis.pool.maxWaitMillis"));
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(300);
        poolConfig.setMaxWaitMillis(MAX_WAIT_MILLIS);
        JEDIS_POOL = new JedisPool(poolConfig, REDIS_HOST, REDIS_PORT);
    }

    Jedis getJedis(int db) {
        int timeoutCount = 0;
        Jedis jedis;
        while (true) {
            try {
                jedis = JEDIS_POOL.getResource();
                if (!Strings.isNullOrEmpty(REDIS_PASSWORD)) {
                    jedis.auth(REDIS_PASSWORD);
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

    public interface JedisAction<T> {
        T action(Jedis jedis);
    }

    void closeResource(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }
}
