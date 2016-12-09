package com.tuotiansudai.worker.monitor.config;

import com.tuotiansudai.client.SmsWrapperClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
@PropertySource(
        ignoreResourceNotFound = true, value = {
        "classpath:ttsd-env.properties",
        "classpath:ttsd-biz.properties"
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

    @Bean
    public SmsWrapperClient smsWrapperClient() {
        return new SmsWrapperClient();
    }
}
