package com.tuotiansudai.mq.consumer.message.config;

import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
@ComponentScan(basePackages = {
        "com.tuotiansudai.util",
        "com.tuotiansudai.cache",
        "com.tuotiansudai.client",
        "com.tuotiansudai.message"
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
}
