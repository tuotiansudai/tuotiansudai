package com.tuotiansudai.client;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;

@Component
public class MybatisRedisCacheWrapperClient extends RedisWrapperClient{

    static Logger logger = Logger.getLogger(MybatisRedisCacheWrapperClient.class);

    @Value("${redis.cache.db}")
    private int redisDb;

    @Value("${redis.cache.second}")
    private int second;

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    private  <T> T execute(JedisAction<T> jedisAction) throws JedisException {
        Jedis jedis = null;
        boolean broken = false;
        try {
            jedis = getJedisPool().getResource();
            if(StringUtils.isNotEmpty(getRedisPassword())){
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
            if(StringUtils.isNotEmpty(getRedisPassword())){
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
        this.setJedisPool(getPool());
        return execute(new JedisAction<byte[]>() {
            @Override
            public byte[] action(Jedis jedis) {
                return jedis.get(key);
            }
        });
    }

    public Object expire(final byte[] key,final int seconds) {
        this.setJedisPool(getPool());
        return execute(new JedisAction<Object>() {
            @Override
            public Object action(Jedis jedis) {
                return jedis.expire(key,seconds);
            }
        });
    }

    public void flushDB() {
        this.setJedisPool(getPool());
        execute(new JedisActionNoResult() {
            @Override
            public void action(Jedis jedis) {
                jedis.flushDB();
            }
        });
    }

    public Long dbSize() {
        this.setJedisPool(getPool());
        return execute(new JedisAction<Long>() {
            @Override
            public Long action(Jedis jedis) {
                return jedis.dbSize();
            }
        });
    }

    public void set(final byte[] key,final byte[] value) {
        this.setJedisPool(getPool());
        execute(new JedisActionNoResult() {
            @Override
            public void action(Jedis jedis) {
                jedis.set(key, value);
            }
        });
    }

}
