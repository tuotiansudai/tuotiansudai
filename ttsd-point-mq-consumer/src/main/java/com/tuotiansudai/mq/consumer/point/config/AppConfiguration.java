package com.tuotiansudai.mq.consumer.point.config;

import com.tuotiansudai.point.job.ImitateLotteryJob;
import com.tuotiansudai.point.service.PointLotteryService;
import com.tuotiansudai.point.service.impl.PointLotteryServiceImpl;
import com.tuotiansudai.rest.client.UserMapperConfiguration;
import com.tuotiansudai.spring.ETCDPropertyPlaceholderConfigurer;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.*;

@Configuration
@ComponentScan(basePackages = {
        "com.tuotiansudai.repository",
        "com.tuotiansudai.cache",
        "com.tuotiansudai.client",
        "com.tuotiansudai.coupon",
        "com.tuotiansudai.membership",
        "com.tuotiansudai.point"
}, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
                PointLotteryService.class,
                ImitateLotteryJob.class,
                PointLotteryServiceImpl.class,
        })
})
@EnableAspectJAutoProxy(exposeProxy = true)
@Import(UserMapperConfiguration.class)
public class AppConfiguration {

    @Bean
    public PropertyPlaceholderConfigurer propertyPlaceholderConfigurer() {
        return new ETCDPropertyPlaceholderConfigurer();
    }
}
