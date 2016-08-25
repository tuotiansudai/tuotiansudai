package com.tuotiansudai.console.activity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@ComponentScan(basePackages = {"com.tuotiansudai.console.activity", "com.tuotiansudai.cache", "com.tuotiansudai.client","com.tuotiansudai.activity","com.tuotiansudai.console.activity.service"})
@PropertySource("classpath:ttsd-env.properties")
public class Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
