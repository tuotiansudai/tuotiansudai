package com.tuotiansudai.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class MQWrapperClientConf {

    @Bean
    @Profile("!test")
    public MQWrapperClient mqWrapperClient() {
        return new MQWrapperClientFormal();
    }

    @Bean
    @Profile("test")
    public MQWrapperClient mqWrapperClientTest() {
        return new MQWrapperClientUT();
    }
}
