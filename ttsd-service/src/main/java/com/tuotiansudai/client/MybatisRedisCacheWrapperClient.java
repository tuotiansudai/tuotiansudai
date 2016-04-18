package com.tuotiansudai.client;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;

import java.util.Set;

@Component
public class MybatisRedisCacheWrapperClient extends AbstractRedisWrapperClient {

    static Logger logger = Logger.getLogger(MybatisRedisCacheWrapperClient.class);

    @Value("${common.mybatis.cache.db}")
    private int redisDb;

    @Value("${common.mybatis.cache.expire.seconds}")
    private int second;

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    private <T> T execute(JedisAction<T> jedisAction) throws JedisException {
        Jedis jedis = null;
        boolean broken = false;
        try {
            jedis = getJedisPool().getResource();
            if (StringUtils.isNotEmpty(getRedisPassword())) {
                jedis.auth(getRedisPassword());
            }
            jedis.select(redisDb);
            return jedisAction.action(jedis);
        } catch (JedisException e) {
            broken = handleJedisException(e);
            throw e;
        } finally {
            closeResource(jedis, broken);
        }
    }

    private void execute(JedisActionNoResult jedisAction) throws JedisException {
        Jedis jedis = null;
        boolean broken = false;
        try {
            jedis = getJedisPool().getResource();
            if (StringUtils.isNotEmpty(getRedisPassword())) {
                jedis.auth(getRedisPassword());
            }
            jedis.select(redisDb);
            jedisAction.action(jedis);
        } catch (JedisException e) {
            broken = handleJedisException(e);
            throw e;
        } finally {
            closeResource(jedis, broken);
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

    public Object expire(final byte[] key, final int seconds) {
        return execute(new JedisAction<Object>() {
            @Override
            public Object action(Jedis jedis) {
                return jedis.expire(key, seconds);
            }
        });
    }

    public void set(final byte[] key, final byte[] value) {
        execute(new JedisActionNoResult() {
            @Override
            public void action(Jedis jedis) {
                jedis.set(key, value);
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
