package com.tuotiansudai.mq.consumer.amount.config;

import com.tuotiansudai.spring.ETCDPropertyPlaceholderConfigurer;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@ComponentScan(basePackages = {
        "com.tuotiansudai.mq.consumer.amount"
        })
@EnableAspectJAutoProxy(exposeProxy = true)
public class AppConfiguration {

    @Bean
    public PropertyPlaceholderConfigurer propertyConfigurer() {
        return new ETCDPropertyPlaceholderConfigurer();
    }

}
