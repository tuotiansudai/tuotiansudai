package com.tuotiansudai.client;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.util.Set;

@Component
public class MybatisRedisCacheWrapperClient extends AbstractRedisWrapperClient {

    static Logger logger = Logger.getLogger(MybatisRedisCacheWrapperClient.class);

    @Value("${common.mybatis.cache.db}")
    protected int redisDb;

    @Value("${common.mybatis.cache.expire.seconds}")
    private int second;

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    private <T> T execute(JedisAction<T> jedisAction) {
        Jedis jedis = null;
        try {
            jedis = getJedis(this.redisDb);
            return jedisAction.action(jedis);
        } finally {
            closeResource(jedis);
        }
    }

    public byte[] get(final byte[] key) {
        return execute(new JedisAction<byte[]>() {
            @Override
            public byte[] action(Jedis jedis) {
                return jedis.get(key);
            }
        });
    }

    public Long expire(final byte[] key, final int seconds) {
        return execute(new JedisAction<Long>() {
            @Override
            public Long action(Jedis jedis) {
                return jedis.expire(key, seconds);
            }
        });
    }

    public String set(final byte[] key, final byte[] value) {
        return execute(new JedisAction<String>() {
            @Override
            public String action(Jedis jedis) {
                return jedis.set(key, value);
            }
        });
    }

    public Set<byte[]> keys(final byte[] key) {
        return execute(new JedisAction<Set<byte[]>>() {
            @Override
            public Set<byte[]> action(Jedis jedis) {
                return jedis.keys(key);
            }
        });
    }
}
