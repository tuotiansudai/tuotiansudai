package com.tuotiansudai.rest.support.client.factory;

import com.tuotiansudai.rest.support.client.annotations.RestClient;
import com.tuotiansudai.rest.support.client.codec.RestErrorDecoder;
import com.tuotiansudai.rest.support.client.interceptors.RequestHeaderInterceptor;
import feign.Feign;
import feign.Logger;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.slf4j.Slf4jLogger;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

public class RestClientFactoryBean<T> implements FactoryBean<T> {
    private ApplicationContext applicationContext;

    private JacksonDecoder jacksonDecoder;
    private JacksonEncoder jacksonEncoder;
    private RestErrorDecoder restErrorDecoder;
    private RequestHeaderInterceptor requestHeaderInterceptor;

    private Class<T> restClientInterface;

    public RestClientFactoryBean() {
    }

    public RestClientFactoryBean(Class<T> restClientInterface) {
        this();
        this.restClientInterface = restClientInterface;
    }

    @Override
    public T getObject() throws Exception {
        return createBean(restClientInterface);
    }

    private T createBean(Class<T> restClientInterface) {
        RestClient restClient = restClientInterface.getAnnotation(RestClient.class);
        Environment environment = applicationContext.getBean(Environment.class);
        String restClientUrl = environment.resolvePlaceholders(restClient.url());
        return Feign.builder()
                .logger(new Slf4jLogger(restClientInterface))
                .encoder(jacksonEncoder)
                .decoder(jacksonDecoder)
                .errorDecoder(restErrorDecoder)
                .requestInterceptor(requestHeaderInterceptor)
                .logLevel(Logger.Level.FULL)
                .target(restClientInterface, restClientUrl);
    }

    @Override
    public Class<T> getObjectType() {
        return this.restClientInterface;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void setJacksonDecoder(JacksonDecoder jacksonDecoder) {
        this.jacksonDecoder = jacksonDecoder;
    }

    public void setJacksonEncoder(JacksonEncoder jacksonEncoder) {
        this.jacksonEncoder = jacksonEncoder;
    }

    public void setRestErrorDecoder(RestErrorDecoder restErrorDecoder) {
        this.restErrorDecoder = restErrorDecoder;
    }

    public void setRequestHeaderInterceptor(RequestHeaderInterceptor requestHeaderInterceptor) {
        this.requestHeaderInterceptor = requestHeaderInterceptor;
    }
}
