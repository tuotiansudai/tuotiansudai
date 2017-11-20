package com.tuotiansudai.console.activity;

import com.tuotiansudai.mq.config.MQProducerConfig;
import com.tuotiansudai.rest.client.UserMapperConfiguration;
import com.tuotiansudai.spring.ETCDPropertyPlaceholderConfigurer;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;


@SpringBootApplication
@ComponentScan(basePackages = {"com.tuotiansudai.console.activity",
        "com.tuotiansudai.cache",
        "com.tuotiansudai.client",
        "com.tuotiansudai.log.service",
        "com.tuotiansudai.repository",
        "com.tuotiansudai.spring"})
@Import({MQProducerConfig.class, UserMapperConfiguration.class})
public class Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public PropertyPlaceholderConfigurer propertyOverrideConfigurer() {
        return new ETCDPropertyPlaceholderConfigurer();
    }
}

