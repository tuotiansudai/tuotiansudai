package com.tuotiansudai.mq.consumer.sms.config;

import com.tuotiansudai.etcd.ETCDPropertySourcesPlaceholderConfigurer;
import com.tuotiansudai.rest.client.UserMapperConfiguration;
import org.springframework.context.annotation.*;

@Configuration
@ComponentScan(basePackages = {
        "com.tuotiansudai.mq.consumer.sms"
})
@EnableAspectJAutoProxy(exposeProxy = true)
@Import(UserMapperConfiguration.class)
public class AppConfiguration {

    @Bean
    public ETCDPropertySourcesPlaceholderConfigurer propertyConfigurer() {
        return new ETCDPropertySourcesPlaceholderConfigurer();
    }
}
