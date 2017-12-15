package com.tuotiansudai.mq.consumer.user.config;

import com.tuotiansudai.etcd.ETCDPropertySourcesPlaceholderConfigurer;
import com.tuotiansudai.rest.client.UserMapperConfiguration;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@ComponentScan(basePackages = {
        "com.tuotiansudai.repository",
        "com.tuotiansudai.util",
        "com.tuotiansudai.cache",
        "com.tuotiansudai.client",
        "com.tuotiansudai.membership"
})
@EnableAspectJAutoProxy(exposeProxy = true)
@Import(UserMapperConfiguration.class)
public class AppConfiguration {

    @Bean
    public ETCDPropertySourcesPlaceholderConfigurer propertyConfigurer() {
        return new ETCDPropertySourcesPlaceholderConfigurer();
    }
}
