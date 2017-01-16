package com.tuotiansudai.rest;

import com.tuotiansudai.rest.client.AskRestClient;
import com.tuotiansudai.rest.client.codec.RestErrorDecoder;
import com.tuotiansudai.rest.client.interceptors.RequestHeaderInterceptor;
import feign.Feign;
import feign.Logger;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.slf4j.Slf4jLogger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource({"classpath:ttsd-env.properties"})
public class FeignClientConfig {
    @Value("${ask.rest.server}")
    private String askRestServer;

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
    public AskRestClient askRestClient(JacksonDecoder jacksonDecoder,
                                       JacksonEncoder jacksonEncoder,
                                       RestErrorDecoder restErrorDecoder,
                                       RequestHeaderInterceptor requestHeaderInterceptor) {
        return Feign.builder()
                .logger(new Slf4jLogger())
                .encoder(jacksonEncoder)
                .decoder(jacksonDecoder)
                .errorDecoder(restErrorDecoder)
                .requestInterceptor(requestHeaderInterceptor)
                .logLevel(Logger.Level.FULL)
                .target(AskRestClient.class, askRestServer);
    }
}
