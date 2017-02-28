package com.tuotiansudai.rest.support.client.factory;

import com.tuotiansudai.rest.support.client.annotations.RestClient;
import com.tuotiansudai.rest.support.client.codec.RestErrorDecoder;
import com.tuotiansudai.rest.support.client.interceptors.RequestHeaderInterceptor;
import feign.Client;
import feign.Request;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.jaxrs.JAXRSContract;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.util.Arrays;
import java.util.Set;

public class RestClientScanner extends ClassPathBeanDefinitionScanner {
    private final Request.Options options;
    private final Client client;
    private final Retryer retryer;
    private final JacksonDecoder jacksonDecoder;
    private final JacksonEncoder jacksonEncoder;
    private final RestErrorDecoder restErrorDecoder;
    private final RequestHeaderInterceptor requestHeaderInterceptor;
    private final JAXRSContract jaxrsContract;
    private final ApplicationContext applicationContext;

    public RestClientScanner(BeanDefinitionRegistry registry, Request.Options options, Client client, Retryer retryer, ApplicationContext applicationContext, JacksonDecoder jacksonDecoder, JacksonEncoder jacksonEncoder, RestErrorDecoder restErrorDecoder, RequestHeaderInterceptor requestHeaderInterceptor, JAXRSContract jaxrsContract) {
        super(registry);
        this.options = options;
        this.client = client;
        this.retryer = retryer;
        this.applicationContext = applicationContext;
        this.jacksonDecoder = jacksonDecoder;
        this.jacksonEncoder = jacksonEncoder;
        this.restErrorDecoder = restErrorDecoder;
        this.requestHeaderInterceptor = requestHeaderInterceptor;
        this.jaxrsContract = jaxrsContract;
    }

    @Override
    protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
        addIncludeFilter(new AnnotationTypeFilter(RestClient.class));
        Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);

        if (beanDefinitions.isEmpty()) {
            logger.warn("No rest client was found in '" + Arrays.toString(basePackages) + "' package. Please check your configuration.");
        } else {
            processBeanDefinitions(beanDefinitions);
        }

        return beanDefinitions;
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent();
    }

    private void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitions) {
        GenericBeanDefinition definition;
        for (BeanDefinitionHolder holder : beanDefinitions) {
            definition = (GenericBeanDefinition) holder.getBeanDefinition();
            definition.getConstructorArgumentValues().addGenericArgumentValue(definition.getBeanClassName());
            definition.setBeanClass(RestClientFactoryBean.class);
            definition.getPropertyValues()
                    .add("applicationContext", this.applicationContext)
                    .add("options", this.options)
                    .add("client", this.client)
                    .add("retryer", this.retryer)
                    .add("jacksonDecoder", this.jacksonDecoder)
                    .add("jacksonEncoder", this.jacksonEncoder)
                    .add("restErrorDecoder", this.restErrorDecoder)
                    .add("requestHeaderInterceptor", this.requestHeaderInterceptor)
                    .add("jaxrsContract", this.jaxrsContract);
        }
    }
}
