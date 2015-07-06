package com.ttsd.redis;

import com.ttsd.aliyun.PropertiesUtils;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

/**
 * Created by Administrator on 2015/7/6.
 */
public class RedisClinet {

    private Jedis jedis;

    public RedisClinet() {
        jedis = new Jedis(PropertiesUtils.getPro("redis.ip"),Integer.parseInt(PropertiesUtils.getPro("redis.port")));
    }

    public Jedis getJedis() {
        return jedis;
    }

    public void setJedis(Jedis jedis) {
        this.jedis = jedis;
    }

}
