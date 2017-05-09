package com.tuotiansudai.util;

import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;

import java.util.ResourceBundle;

public class MybatisRedisCacheWrapperClient extends AbstractRedisWrapperClient {

    static Logger logger = Logger.getLogger(MybatisRedisCacheWrapperClient.class);

    private static int MYBATIS_DB;

    static {
        ResourceBundle bundle = ResourceBundle.getBundle("ttsd-env");
        MYBATIS_DB = Integer.parseInt(bundle.getString("common.mybatis.cache.db"));
    }

    public String clearMybatisCache() {
        Jedis jedis = null;
        try {
            jedis = getJedis(MYBATIS_DB);
            return jedis.flushDB();
        } finally {
            closeResource(jedis);
        }
    }

    private <T> T execute(JedisAction<T> jedisAction) {
        Jedis jedis = null;
        try {
            jedis = getJedis(MYBATIS_DB);
            return jedisAction.action(jedis);
        } finally {
            closeResource(jedis);
        }
    }
}
