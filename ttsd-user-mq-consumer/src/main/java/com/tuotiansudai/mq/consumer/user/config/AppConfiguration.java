package com.tuotiansudai.mq.consumer.user.config;

import com.tuotiansudai.rest.client.UserMapperConfiguration;
import com.tuotiansudai.spring.ETCDPropertyPlaceholderConfigurer;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.*;

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
    public PropertyPlaceholderConfigurer propertyConfigurer() {
        return new ETCDPropertyPlaceholderConfigurer();
    }
}
