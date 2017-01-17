package com.tuotiansudai.rest.support.client.factory;

import com.tuotiansudai.rest.support.client.annotations.RestClient;
import com.tuotiansudai.rest.support.client.codec.RestErrorDecoder;
import com.tuotiansudai.rest.support.client.interceptors.RequestHeaderInterceptor;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
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
    private final JacksonDecoder jacksonDecoder;
    private final JacksonEncoder jacksonEncoder;
    private final RestErrorDecoder restErrorDecoder;
    private final RequestHeaderInterceptor requestHeaderInterceptor;
    private final ApplicationContext applicationContext;

    public RestClientScanner(BeanDefinitionRegistry registry, ApplicationContext applicationContext, JacksonDecoder jacksonDecoder, JacksonEncoder jacksonEncoder, RestErrorDecoder restErrorDecoder, RequestHeaderInterceptor requestHeaderInterceptor) {
        super(registry);
        this.applicationContext = applicationContext;
        this.jacksonDecoder = jacksonDecoder;
        this.jacksonEncoder = jacksonEncoder;
        this.restErrorDecoder = restErrorDecoder;
        this.requestHeaderInterceptor = requestHeaderInterceptor;
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
                    .add("jacksonDecoder", this.jacksonDecoder)
                    .add("jacksonEncoder", this.jacksonEncoder)
                    .add("restErrorDecoder", this.restErrorDecoder)
                    .add("requestHeaderInterceptor", this.requestHeaderInterceptor);
        }
    }
}
