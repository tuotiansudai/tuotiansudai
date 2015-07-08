package com.ttsd.redis;

import com.ttsd.aliyun.PropertiesUtils;
import redis.clients.jedis.Jedis;

/**
 * Created by Administrator on 2015/7/6.
 */
public class RedisClinet {

    private Jedis jedis;

    public RedisClinet() {
        jedis = new Jedis(PropertiesUtils.getPro("redis.ip"),Integer.parseInt(PropertiesUtils.getPro("redis.port")));
        jedis.auth(PropertiesUtils.getPro("redis.password"));
        jedis.select(Integer.parseInt(PropertiesUtils.getPro("redis.db")));
    }

    public Jedis getJedis() {
        return jedis;
    }

    public void setJedis(Jedis jedis) {
        this.jedis = jedis;
    }

}
