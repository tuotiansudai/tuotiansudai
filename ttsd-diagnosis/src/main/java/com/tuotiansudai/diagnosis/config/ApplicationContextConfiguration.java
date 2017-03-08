package com.tuotiansudai.diagnosis.config;

import com.tuotiansudai.client.PayWrapperClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;

@Configuration
public class ApplicationContextConfiguration {

    @Value("${common.redis.host}")
    private String redisHost;
    @Value("${common.redis.port}")
    private int redisPort;
    @Value("${common.redis.password}")
    private String redisPassword;
    @Value("${common.redis.db}")
    private int redisDB;

    @Bean
    public Jedis jedis() {
        Jedis jedis = new Jedis(this.redisHost, this.redisPort);
        if (StringUtils.isNotBlank(redisPassword)) {
            jedis.auth(redisPassword);
        }
        jedis.connect();
        jedis.select(redisDB);
        return jedis;
    }

    @Bean
    public PayWrapperClient payWrapperClient(){
        return new PayWrapperClient();
    }

}
