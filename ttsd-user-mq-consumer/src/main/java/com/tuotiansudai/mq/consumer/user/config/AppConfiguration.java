package com.tuotiansudai.mq.consumer.user.config;

import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.service.impl.ExperienceServiceImpl;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
@ComponentScan(basePackages = {
        "com.tuotiansudai.repository",
        "com.tuotiansudai.util",
        "com.tuotiansudai.cache",
        "com.tuotiansudai.client",
        "com.tuotiansudai.membership"
})
@PropertySource(
        ignoreResourceNotFound = true, value = {
        "classpath:ttsd-env.properties",
        "classpath:ttsd-biz.properties"
})
@EnableAspectJAutoProxy(exposeProxy = true)
public class AppConfiguration {

    @Bean
    public PropertySourcesPlaceholderConfigurer propertyConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean(name="experienceBillService")
    public ExperienceServiceImpl experienceService() {
        return new ExperienceServiceImpl();
    }

    @Bean
    public RedisWrapperClient redisWrapperClient(PropertySourcesPlaceholderConfigurer configurer) {
        return new RedisWrapperClient();
    }

    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(10);
        jedisPoolConfig.setMaxWaitMillis(5000);
        return jedisPoolConfig;
    }
}
