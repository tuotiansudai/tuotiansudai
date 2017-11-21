package com.tuotiansudai.mq.consumer.auditLog.config;

import com.tuotiansudai.spring.ETCDPropertyPlaceholderConfigurer;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
@ComponentScan(basePackages = {
        "com.tuotiansudai.log.repository",
        "com.tuotiansudai.util",
        "com.tuotiansudai.client"
})
@EnableAspectJAutoProxy(exposeProxy = true)
public class AppConfiguration {

    @Bean
    public PropertyPlaceholderConfigurer propertyConfigurer() {
        return new ETCDPropertyPlaceholderConfigurer();
    }
}
