package com.ttsd.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
        return jedisTemplate.lpush(key, values);
    }

    public void hmset(final String key, final Map map) {
        jedisTemplate.setJedisPool(getPool());
        jedisTemplate.hmset(key, map);
    }

    public void hset(final String key, final String hkey, final String value) {
        jedisTemplate.setJedisPool(getPool());
        jedisTemplate.hset(key, hkey, value);
    }

    public Long hdel(final String key, final String... hkeys) {
        jedisTemplate.setJedisPool(getPool());
        return jedisTemplate.hdel(key, hkeys);
    }

    public int hlen(final String key) {
        jedisTemplate.setJedisPool(getPool());
        return jedisTemplate.hlen(key);
    }

    public Set hkeys(final String key) {
        jedisTemplate.setJedisPool(getPool());
        return jedisTemplate.hkeys(key);
    }

    public List hmget(final String key, final String... hkey) {
        jedisTemplate.setJedisPool(getPool());
        return jedisTemplate.hmget(key, hkey);
    }

    public String hget(final String key, final String hkey) {
        jedisTemplate.setJedisPool(getPool());
        return jedisTemplate.hget(key, hkey);
    }
}
