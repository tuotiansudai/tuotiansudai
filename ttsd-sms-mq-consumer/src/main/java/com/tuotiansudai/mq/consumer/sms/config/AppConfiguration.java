package com.tuotiansudai.mq.consumer.sms.config;

import com.tuotiansudai.etcd.ETCDPropertySourcesPlaceholderConfigurer;
import org.springframework.context.annotation.*;

@Configuration
@ComponentScan(basePackages = {
        "com.tuotiansudai.sms",
        "com.tuotiansudai.client",
        "com.tuotiansudai.util",
        "com.tuotiansudai.cache"
})
@EnableAspectJAutoProxy(exposeProxy = true)
public class AppConfiguration {

    @Bean
    public ETCDPropertySourcesPlaceholderConfigurer propertyConfigurer() {
        return new ETCDPropertySourcesPlaceholderConfigurer();
    }
}
