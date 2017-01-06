package com.tuotiansudai.rest;

import com.tuotiansudai.rest.client.codec.RestErrorDecoder;
import com.tuotiansudai.rest.client.interceptors.RequestHeaderInterceptor;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;

@EnableAutoConfiguration
@EnableFeignClients
@PropertySource({"classpath:feign.properties"})
public class FeignClientConfig {
    @Bean
    public RequestHeaderInterceptor requestHeaderInterceptor() {
        return new RequestHeaderInterceptor();
    }

    @Bean
    public RestErrorDecoder restErrorDecoder() {
        return new RestErrorDecoder();
    }
}
