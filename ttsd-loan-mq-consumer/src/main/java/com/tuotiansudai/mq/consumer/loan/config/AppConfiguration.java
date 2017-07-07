package com.tuotiansudai.mq.consumer.loan.config;

import com.tuotiansudai.job.JobManager;
import com.tuotiansudai.quartz.JobStoreBuilder;
import com.tuotiansudai.quartz.SchedulerBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import javax.sql.DataSource;

@Configuration
@ComponentScan(basePackages = {
        "com.tuotiansudai.service",
        "com.tuotiansudai.transfer",
        "com.tuotiansudai.util",
        "com.tuotiansudai.client",
        "com.tuotiansudai.coupon",
        "com.tuotiansudai.membership",
        "com.tuotiansudai.log"
        })
@PropertySource(ignoreResourceNotFound = true, value = {"classpath:ttsd-env.properties", "classpath:ttsd-biz.properties"})
@EnableAspectJAutoProxy(exposeProxy = true)
public class AppConfiguration {

    @Bean
    public PropertySourcesPlaceholderConfigurer propertyConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public JobStoreBuilder jobStoreBuilder(@Qualifier("hikariCPJobDataSource") DataSource dataSource) {
        return new JobStoreBuilder(dataSource);
    }

    @Bean
    public SchedulerBuilder schedulerBuilder(JobStoreBuilder jobStoreBuilder) {
        return new SchedulerBuilder(jobStoreBuilder);
    }

    @Bean
    public JobManager jobManager(SchedulerBuilder schedulerBuilder) {
        return new JobManager(schedulerBuilder);
    }
}
