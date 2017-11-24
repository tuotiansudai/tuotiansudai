package com.tuotiansudai.rest.support.client.factory;

import com.tuotiansudai.etcd.ETCDConfigReader;
import com.tuotiansudai.rest.support.client.annotations.RestClient;
import com.tuotiansudai.rest.support.client.codec.RestErrorDecoder;
import com.tuotiansudai.rest.support.client.interceptors.RequestHeaderInterceptor;
import feign.*;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.jaxrs.JAXRSContract;
import feign.slf4j.Slf4jLogger;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;

public class RestClientFactoryBean<T> implements FactoryBean<T> {
    private ApplicationContext applicationContext;
    private Request.Options options;
    private Client client;
    private Retryer retryer;
    private JacksonDecoder jacksonDecoder;
    private JacksonEncoder jacksonEncoder;
    private RestErrorDecoder restErrorDecoder;
    private RequestHeaderInterceptor requestHeaderInterceptor;
    private JAXRSContract jaxrsContract;
    private Class<T> restClientInterface;
    private ETCDConfigReader etcdConfigReader = ETCDConfigReader.getReader();

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
        String restClientUrl = etcdConfigReader.getValue(restClient.url());
        return Feign.builder()
                .logger(new Slf4jLogger(restClientInterface))
                .client(client)
                .options(options)
                .retryer(retryer)
                .encoder(jacksonEncoder)
                .decoder(jacksonDecoder)
                .errorDecoder(restErrorDecoder)
                .requestInterceptor(requestHeaderInterceptor)
                .logLevel(Logger.Level.FULL)
                .contract(jaxrsContract)
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

    public void setOptions(Request.Options options) {
        this.options = options;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setRetryer(Retryer retryer) {
        this.retryer = retryer;
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

    public void setJaxrsContract(JAXRSContract jaxrsContract) {
        this.jaxrsContract = jaxrsContract;
    }
}
