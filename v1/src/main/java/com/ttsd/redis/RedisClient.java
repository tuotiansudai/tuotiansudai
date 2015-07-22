package com.ttsd.redis;

import com.ttsd.aliyun.PropertiesUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

@Service
public class RedisClient {
    @Value("${redis.ip}")
    private String redisIp;
    @Value("${redis.port}")
    private Integer redisPort;
    @Value("${redis.password}")
    private String redisPassword;
    @Value("${redis.db}")
    private Integer redisDb;

    private static Jedis jedis;

    public  Jedis getJedis() {
        if (jedis == null) {
            jedis = new Jedis(redisIp, redisPort);
            if (redisPassword != null && !"".equals(redisPassword)) {
                jedis.auth(redisPassword);
            }
            jedis.select(redisDb);
        }
        return jedis;
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

    public String getRedisPassword() {
        return redisPassword;
    }

    public void setRedisPassword(String redisPassword) {
        this.redisPassword = redisPassword;
    }

    public Integer getRedisDb() {
        return redisDb;
    }

    public void setRedisDb(Integer redisDb) {
        this.redisDb = redisDb;
    }

    public static void setJedis(Jedis jedis) {
        RedisClient.jedis = jedis;
    }
}
