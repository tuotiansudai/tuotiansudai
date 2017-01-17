package com.tuotiansudai.rest;

import com.tuotiansudai.rest.support.client.codec.RestErrorDecoder;
import com.tuotiansudai.rest.support.client.factory.RestClientScannerConfigurer;
import com.tuotiansudai.rest.support.client.interceptors.RequestHeaderInterceptor;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource({"classpath:ttsd-env.properties", "classpath:ttsd-env-test.properties"})
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
    public JacksonEncoder jacksonEncoder() {
        return new JacksonEncoder();
    }

    @Bean
    public JacksonDecoder jacksonDecoder() {
        return new JacksonDecoder();
    }

    @Bean
    public RestClientScannerConfigurer restClientScannerConfigurer(
            JacksonDecoder jacksonDecoder,
            JacksonEncoder jacksonEncoder,
            RestErrorDecoder restErrorDecoder,
            RequestHeaderInterceptor requestHeaderInterceptor) {
        RestClientScannerConfigurer configurer = new RestClientScannerConfigurer(
                jacksonDecoder,
                jacksonEncoder,
                restErrorDecoder,
                requestHeaderInterceptor);
        configurer.setBasePackages("com.tuotiansudai.rest.client");
        return configurer;
    }
}
