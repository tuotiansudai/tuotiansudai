package com.tuotiansudai.mq.consumer.message.config;

import com.tuotiansudai.spring.ETCDPropertyPlaceholderConfigurer;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
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
@EnableAspectJAutoProxy(exposeProxy = true)
public class AppConfiguration {

    @Bean
    public PropertyPlaceholderConfigurer propertyConfigurer() {
        return new ETCDPropertyPlaceholderConfigurer();
    }
}
