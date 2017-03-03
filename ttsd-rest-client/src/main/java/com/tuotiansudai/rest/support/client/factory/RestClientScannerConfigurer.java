package com.tuotiansudai.rest.support.client.factory;

import com.tuotiansudai.rest.support.client.codec.RestErrorDecoder;
import com.tuotiansudai.rest.support.client.interceptors.RequestHeaderInterceptor;
import feign.Client;
import feign.Request;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.jaxrs.JAXRSContract;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class RestClientScannerConfigurer implements BeanDefinitionRegistryPostProcessor, ApplicationContextAware {
    private final Request.Options options;
    private final Client client;
    private final Retryer retryer;
    private final JacksonDecoder jacksonDecoder;
    private final JacksonEncoder jacksonEncoder;
    private final RestErrorDecoder restErrorDecoder;
    private final RequestHeaderInterceptor requestHeaderInterceptor;
    private final JAXRSContract jaxrsContract;
    private ApplicationContext applicationContext;
    private String[] basePackages;

    public RestClientScannerConfigurer(Request.Options options, Client client, Retryer retryer, JacksonDecoder jacksonDecoder, JacksonEncoder jacksonEncoder, RestErrorDecoder restErrorDecoder, RequestHeaderInterceptor requestHeaderInterceptor, JAXRSContract jaxrsContract) {
        this.options = options;
        this.client = client;
        this.retryer = retryer;
        this.jacksonDecoder = jacksonDecoder;
        this.jacksonEncoder = jacksonEncoder;
        this.restErrorDecoder = restErrorDecoder;
        this.requestHeaderInterceptor = requestHeaderInterceptor;
        this.jaxrsContract = jaxrsContract;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        RestClientScanner scanner = new RestClientScanner(registry, options, client, retryer, applicationContext, jacksonDecoder, jacksonEncoder, restErrorDecoder, requestHeaderInterceptor, jaxrsContract);
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
