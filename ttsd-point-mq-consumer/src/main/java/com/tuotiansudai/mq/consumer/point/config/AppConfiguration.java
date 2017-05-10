package com.tuotiansudai.mq.consumer.point.config;

import com.tuotiansudai.point.job.ImitateLotteryJob;
import com.tuotiansudai.point.service.PointLotteryService;
import com.tuotiansudai.point.service.impl.PointLotteryServiceImpl;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import redis.clients.jedis.JedisPoolConfig;

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
@PropertySource(
        ignoreResourceNotFound = true, value = {
        "classpath:ttsd-env.properties",
        "classpath:ttsd-biz.properties"
})
@EnableAspectJAutoProxy(exposeProxy = true)
public class AppConfiguration {

    @Bean
    public PropertySourcesPlaceholderConfigurer propertyConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
