package com.tuotiansudai.rest;

import com.tuotiansudai.rest.support.client.codec.RestErrorDecoder;
import com.tuotiansudai.rest.support.client.factory.RestClientScannerConfigurer;
import com.tuotiansudai.rest.support.client.interceptors.RequestHeaderInterceptor;
import feign.Request;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import static java.util.concurrent.TimeUnit.SECONDS;

@Configuration
@PropertySource(value = {"classpath:ttsd-env.properties", "classpath:ttsd-env-test.properties"}, ignoreResourceNotFound = true)
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
    public Request.Options feignRequestOptions() {
        return new Request.Options(5000, 10000);
    }

    @Bean
    public Retryer retryer() {
        return new Retryer.Default(100, SECONDS.toMillis(1), 1);
    }

    @Bean
    public RestClientScannerConfigurer restClientScannerConfigurer(
            Request.Options feignRequestOptions, Retryer retryer,
            JacksonDecoder jacksonDecoder, JacksonEncoder jacksonEncoder, RestErrorDecoder restErrorDecoder,
            RequestHeaderInterceptor requestHeaderInterceptor) {
        RestClientScannerConfigurer configurer = new RestClientScannerConfigurer(
                feignRequestOptions, retryer,
                jacksonDecoder, jacksonEncoder,
                restErrorDecoder, requestHeaderInterceptor);
        configurer.setBasePackages("com.tuotiansudai.rest.client");
        return configurer;
    }
}
