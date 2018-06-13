package com.tuotiansudai.rest;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.tuotiansudai.rest.databind.date.deserializer.DateDeserializer;
import com.tuotiansudai.rest.support.client.codec.RestErrorDecoder;
import com.tuotiansudai.rest.support.client.factory.RestClientScannerConfigurer;
import com.tuotiansudai.rest.support.client.interceptors.RequestHeaderInterceptor;
import feign.Request;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.jaxrs.JAXRSContract;
import feign.okhttp.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.util.concurrent.TimeUnit.SECONDS;

@Configuration
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
        List<Module> moduleList = new ArrayList<>();
        SimpleModule sm = new SimpleModule();
        sm.addDeserializer(Date.class, new DateDeserializer());
        moduleList.add(sm);
        return new JacksonDecoder(moduleList);
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
    public JAXRSContract jaxrsContract() {
        return new JAXRSContract();
    }

    @Bean
    public RestClientScannerConfigurer restClientScannerConfigurer(
            Request.Options feignRequestOptions, Retryer retryer,
            JacksonDecoder jacksonDecoder, JacksonEncoder jacksonEncoder, RestErrorDecoder restErrorDecoder,
            RequestHeaderInterceptor requestHeaderInterceptor, JAXRSContract jaxrsContract) {
        OkHttpClient okHttpClient = new OkHttpClient();
        RestClientScannerConfigurer configurer = new RestClientScannerConfigurer(
                feignRequestOptions, okHttpClient, retryer,
                jacksonDecoder, jacksonEncoder,
                restErrorDecoder, requestHeaderInterceptor, jaxrsContract);
        configurer.setBasePackages("com.tuotiansudai.rest.client");
        return configurer;
    }
}
