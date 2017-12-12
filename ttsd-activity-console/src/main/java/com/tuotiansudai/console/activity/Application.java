package com.tuotiansudai.console.activity;

import com.tuotiansudai.etcd.ETCDPropertySourcesPlaceholderConfigurer;
import com.tuotiansudai.mq.config.MQProducerConfig;
import com.tuotiansudai.rest.client.UserMapperConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;


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
    public ETCDPropertySourcesPlaceholderConfigurer propertyConfigurer() {
        return new ETCDPropertySourcesPlaceholderConfigurer();
    }
}

