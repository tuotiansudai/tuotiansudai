package com.tuotiansudai.etcd;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.*;

import java.io.IOException;
import java.lang.reflect.Field;

public class ETCDPropertySourcesPlaceholderConfigurer extends PropertySourcesPlaceholderConfigurer {

    private ETCDConfigReader etcdConfigReader = ETCDConfigReader.getReader();

    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        try {
            Field propertySourcesField = PropertySourcesPlaceholderConfigurer.class.getDeclaredField("propertySources");
            propertySourcesField.setAccessible(true);
            MutablePropertySources propertySources = (MutablePropertySources) propertySourcesField.get(this);

            Field environmentField = PropertySourcesPlaceholderConfigurer.class.getDeclaredField("environment");
            environmentField.setAccessible(true);
            Environment environment = (Environment) environmentField.get(this);

            Field appliedPropertySourcesField = PropertySourcesPlaceholderConfigurer.class.getDeclaredField("appliedPropertySources");
            appliedPropertySourcesField.setAccessible(true);

            if (propertySources == null) {
                propertySources = new MutablePropertySources();
                if (environment != null) {
                    propertySources.addLast(
                            new PropertySource<Environment>(ENVIRONMENT_PROPERTIES_PROPERTY_SOURCE_NAME, environment) {
                                @Override
                                public String getProperty(String key) {
                                    return this.source.getProperty(key);
                                }
                            }
                    );
                }
                try {
                    PropertySource<?> localPropertySource =
                            new PropertiesPropertySource(LOCAL_PROPERTIES_PROPERTY_SOURCE_NAME, mergeProperties());
                    if (this.localOverride) {
                        propertySources.addFirst(localPropertySource);
                    }
                    else {
                        propertySources.addLast(localPropertySource);
                    }
                }
                catch (IOException ex) {
                    throw new BeanInitializationException("Could not load properties", ex);
                }
                propertySources.addLast(new PropertySource<ETCDConfigReader>("etcdProperties", etcdConfigReader) {
                    public String getProperty(String key) {
                        return this.source.getValue(key);
                    }
                });
            }
            propertySourcesField.set(this, propertySources);
            this.processProperties(beanFactory, new PropertySourcesPropertyResolver(propertySources));
            appliedPropertySourcesField.set(this, propertySources);
        } catch (IllegalAccessException | NoSuchFieldException ignored) {
        }

    }
}