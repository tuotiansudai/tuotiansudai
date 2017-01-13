package com.tuotiansudai.rest;

import com.tuotiansudai.rest.client.codec.RestErrorDecoder;
import com.tuotiansudai.rest.client.interceptors.RequestHeaderInterceptor;
import feign.Logger;
import org.springframework.boot.autoconfigure.web.HttpMessageConvertersAutoConfiguration;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.feign.FeignAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Import({FeignAutoConfiguration.class, HttpMessageConvertersAutoConfiguration.class})
@EnableFeignClients
@PropertySource({"classpath:feign.properties", "classpath:ttsd-env.properties"})
public class FeignClientConfig {
    @Bean
    public RequestHeaderInterceptor requestHeaderInterceptor() {
        return new RequestHeaderInterceptor();
    }

    @Bean
    public RestErrorDecoder restErrorDecoder() {
        return new RestErrorDecoder();
    }

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}
