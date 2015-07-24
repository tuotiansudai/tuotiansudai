package com.ttsd.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.Resource;

@Service
public class RedisClient {
    @Value("${redis.ip}")
    private String redisIp;
    @Value("${redis.port}")
    private Integer redisPort;
    @Resource
    private JedisTemplate jedisTemplate;

    private static JedisPool pool = null;

    public JedisPool getPool() {
        if (pool == null) {
            JedisPoolConfig config = new JedisPoolConfig();

            pool = new JedisPool(config, redisIp, redisPort);
        }
        return pool;
    }


    public String getRedisIp() {
        return redisIp;
    }

    public void setRedisIp(String redisIp) {
        this.redisIp = redisIp;
    }

    public Integer getRedisPort() {
        return redisPort;
    }

    public void setRedisPort(Integer redisPort) {
        this.redisPort = redisPort;
    }
    public String get(String key) {
        jedisTemplate.setJedisPool(getPool());
        return jedisTemplate.get(key);
    }

    public boolean exists(String key) {
        jedisTemplate.setJedisPool(getPool());
        return jedisTemplate.exists(key);
    }

    public void setex(String key, String value, int seconds) {
        jedisTemplate.setJedisPool(getPool());
        jedisTemplate.setex(key, value, seconds);
    }

    public void set(String key, String value) {
        jedisTemplate.setJedisPool(getPool());
        jedisTemplate.set(key, value);
    }

    public boolean del(String key) {
        jedisTemplate.setJedisPool(getPool());
        return jedisTemplate.del(key);
    }
    public Long lpush(final String key, final String... values) {
        jedisTemplate.setJedisPool(getPool());
        return jedisTemplate.lpush(key,values);
    }
}
