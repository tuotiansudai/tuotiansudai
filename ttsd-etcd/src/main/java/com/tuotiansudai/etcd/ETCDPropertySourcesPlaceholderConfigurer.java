package com.tuotiansudai.etcd;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.PropertySourcesPropertyResolver;

import java.lang.reflect.Field;

public class ETCDPropertySourcesPlaceholderConfigurer extends PropertySourcesPlaceholderConfigurer {

    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        super.postProcessBeanFactory(beanFactory);

        try {
            Field field = PropertySourcesPlaceholderConfigurer.class.getDeclaredField("propertySources");
            field.setAccessible(true);
            MutablePropertySources propertySources = (MutablePropertySources) field.get(this);
            propertySources.addLast(new PropertySource<ETCDConfigReader>("etcdProperties", new ETCDConfigReader()) {
                public String getProperty(String key) {
                    return this.source.getProperties(key);
                }
            });
            this.processProperties(beanFactory, new PropertySourcesPropertyResolver(propertySources));
        } catch (IllegalAccessException | NoSuchFieldException ignored) {
        }

    }
}