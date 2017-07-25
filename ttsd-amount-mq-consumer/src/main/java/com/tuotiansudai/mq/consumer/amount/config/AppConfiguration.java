package com.tuotiansudai.mq.consumer.amount.config;

import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@ComponentScan(basePackages = {
        "com.tuotiansudai.mq.consumer.amount"

        })
@PropertySource(ignoreResourceNotFound = true, value = {"classpath:ttsd-env.properties", "classpath:ttsd-biz.properties"})
@EnableAspectJAutoProxy(exposeProxy = true)
public class AppConfiguration {

    @Bean
    public PropertySourcesPlaceholderConfigurer propertyConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

}
