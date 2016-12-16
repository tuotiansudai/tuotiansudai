package com.tuotiansudai.mq.tools;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import redis.clients.jedis.Jedis;

public class RedisClient {
    @Value("${common.redis.host}")
    private String redisHost;
    @Value("${common.redis.port}")
    private int redisPort;
    @Value("${common.redis.password}")
    private String redisPassword;
    @Value("${common.redis.db}")
    private int redisDB;

    public Jedis newJedis() {
        Jedis jedis = new Jedis(this.redisHost, this.redisPort);
        if (StringUtils.isNotBlank(redisPassword)) {
            jedis.auth(redisPassword);
        }
        jedis.connect();
        jedis.select(redisDB);
        return jedis;
    }
}
