package com.tuotiansudai.rest.support.client.factory;

import com.tuotiansudai.rest.support.client.codec.RestErrorDecoder;
import com.tuotiansudai.rest.support.client.interceptors.RequestHeaderInterceptor;
import feign.Request;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class RestClientScannerConfigurer implements BeanDefinitionRegistryPostProcessor, ApplicationContextAware {
    private final Request.Options options;
    private final Retryer retryer;
    private final JacksonDecoder jacksonDecoder;
    private final JacksonEncoder jacksonEncoder;
    private final RestErrorDecoder restErrorDecoder;
    private final RequestHeaderInterceptor requestHeaderInterceptor;
    private ApplicationContext applicationContext;
    private String[] basePackages;

    public RestClientScannerConfigurer(Request.Options options, Retryer retryer, JacksonDecoder jacksonDecoder, JacksonEncoder jacksonEncoder, RestErrorDecoder restErrorDecoder, RequestHeaderInterceptor requestHeaderInterceptor) {
        this.options = options;
        this.retryer = retryer;
        this.jacksonDecoder = jacksonDecoder;
        this.jacksonEncoder = jacksonEncoder;
        this.restErrorDecoder = restErrorDecoder;
        this.requestHeaderInterceptor = requestHeaderInterceptor;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        RestClientScanner scanner = new RestClientScanner(registry, options, retryer, applicationContext, jacksonDecoder, jacksonEncoder, restErrorDecoder, requestHeaderInterceptor);
        scanner.scan(basePackages);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void setBasePackages(String... basePackages) {
        this.basePackages = basePackages;
    }
}
