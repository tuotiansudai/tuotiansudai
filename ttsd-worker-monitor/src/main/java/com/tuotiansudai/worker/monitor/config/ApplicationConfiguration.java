package com.tuotiansudai.worker.monitor.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
@ComponentScan(basePackages = {
        "com.tuotiansudai.client",
        "com.tuotiansudai.util"
})
public class ApplicationConfiguration {

    @Value("${common.redis.host}")
    private String redisHost;

    @Value("${common.redis.port}")
    private int redisPort;

    @Value("${common.redis.password}")
    private String redisPassword;

    @Value("${common.redis.db}")
    protected int redisDb;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        JedisConnectionFactory factory = new JedisConnectionFactory();
        factory.setHostName(redisHost);
        factory.setPort(redisPort);
        factory.setDatabase(redisDb);
        factory.setPassword(redisPassword);
        return factory;
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        return new StringRedisTemplate(redisConnectionFactory);
    }

}
