package com.tuotiansudai.rest.ask.config;

import com.tuotiansudai.client.RedisWrapperClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import redis.clients.jedis.JedisPoolConfig;


@Configuration
@PropertySource(
        ignoreResourceNotFound = true, value = {
        "classpath:ttsd-env.properties",
        "classpath:ttsd-biz.properties",
        "classpath:ask-redis-key-template.properties"
})
public class WebAppConfiguration {

    @Bean
    public PropertySourcesPlaceholderConfigurer propertyConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public JedisPoolConfig jedisPoolConfig(@Value("${ask.jedis.pool.maxTotal}") int maxTotal,
                                           @Value("${common.jedis.pool.maxWaitMillis}") int maxWaitMillis){
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(maxTotal);
        jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
        return jedisPoolConfig;
    }

    @Bean
    public RedisWrapperClient redisWrapperClient(PropertySourcesPlaceholderConfigurer configurer) {
        return new RedisWrapperClient();
    }

}
