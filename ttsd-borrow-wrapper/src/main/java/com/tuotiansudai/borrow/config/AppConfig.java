package com.tuotiansudai.borrow.config;

import com.tuotiansudai.etcd.ETCDPropertySourcesPlaceholderConfigurer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan(basePackages = {"com.tuotiansudai.rest.client"})
@MapperScan(basePackages = {"com.tuotiansudai.repository.mapper"})
@EnableTransactionManagement
public class AppConfig {
    @Bean
    public ETCDPropertySourcesPlaceholderConfigurer placeholderConfigurer(){
        return new ETCDPropertySourcesPlaceholderConfigurer();
    }
}
