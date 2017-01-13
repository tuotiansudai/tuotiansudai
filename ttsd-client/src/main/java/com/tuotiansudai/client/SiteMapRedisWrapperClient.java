package com.tuotiansudai.client;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.util.Map;

@Component
public class SiteMapRedisWrapperClient extends AbstractRedisWrapperClient {

    static Logger logger = Logger.getLogger(SiteMapRedisWrapperClient.class);

    @Value("${common.sitemap.db}")
    private int sitemapRedisDb;

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
}