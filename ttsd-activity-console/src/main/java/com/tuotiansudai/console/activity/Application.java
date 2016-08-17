package com.tuotiansudai.console.activity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@ComponentScan(basePackages = {"com.tuotiansudai.console.activity", "com.tuotiansudai.cache", "com.tuotiansudai.client"})
@PropertySource("classpath:ttsd-env.properties")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
