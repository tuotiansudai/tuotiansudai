package com.tuotiansudai.client;

import com.tuotiansudai.service.impl.JedisServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2015/8/7.
 */
@Component
public class RedisWrapperClient {

    static Logger logger = Logger.getLogger(RedisWrapperClient.class);

    @Value("${redis.ip}")
    private String redisIp;

    @Value("${redis.port}")
    private Integer redisPort;

    @Resource
    private JedisServiceImpl jedisService;

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
        jedisService.setJedisPool(getPool());
        return jedisService.get(key);
    }

    public boolean exists(String key) {
        jedisService.setJedisPool(getPool());
        return jedisService.exists(key);
    }

    public void setex(String key, int seconds, String value) {
        jedisService.setJedisPool(getPool());
        jedisService.setex(key, value, seconds);
    }

    public void set(String key, String value) {
        jedisService.setJedisPool(getPool());
        jedisService.set(key, value);
    }

    public boolean del(String key) {
        jedisService.setJedisPool(getPool());
        return jedisService.del(key);
    }
    public Long lpush(final String key, final String... values) {
        jedisService.setJedisPool(getPool());
        return jedisService.lpush(key,values);
    }

}
