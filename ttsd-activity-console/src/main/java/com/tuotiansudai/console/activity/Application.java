package com.tuotiansudai.console.activity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.session.SessionAutoConfiguration;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication(exclude = {SessionAutoConfiguration.class})
@ComponentScan(basePackages = {"com.tuotiansudai.console.activity", "com.tuotiansudai.activity.service","com.tuotiansudai.cache", "com.tuotiansudai.client", "com.tuotiansudai.spring"})
@PropertySource(value = {"classpath:ttsd-env.properties", "classpath:ttsd-biz.properties"})
public class Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
