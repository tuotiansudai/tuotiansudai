package com.tuotiansudai.mq.consumer.loan.config;

import com.squareup.okhttp.OkHttpClient;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.job.JobManager;
import com.tuotiansudai.quartz.JobStoreBuilder;
import com.tuotiansudai.quartz.SchedulerBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import redis.clients.jedis.JedisPoolConfig;

import javax.sql.DataSource;

@Configuration
@ComponentScan(basePackages = {
        "com.tuotiansudai.service",
        "com.tuotiansudai.util",
        "com.tuotiansudai.client",
        "com.tuotiansudai.coupon",
        "com.tuotiansudai.membership",
        "com.tuotiansudai.log",
        "com.tuotiansudai.anxin",
        "com.tuotiansudai.contract",
        "com.tuotiansudai.cfca"
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

    @Bean
    public RedisWrapperClient redisWrapperClient(PropertySourcesPlaceholderConfigurer configurer) {
        return new RedisWrapperClient();
    }

    @Bean
    public OkHttpClient httpClient() {
        return new OkHttpClient();
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

    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(10);
        jedisPoolConfig.setMaxWaitMillis(5000);
        return jedisPoolConfig;
    }
}
