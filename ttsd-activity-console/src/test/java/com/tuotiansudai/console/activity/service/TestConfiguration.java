package com.tuotiansudai.console.activity.service;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@org.springframework.boot.test.context.TestConfiguration
@ComponentScan("com.tuotiansudai.console.activity.service.ExportService")
@MapperScan("com.tuotiansudai.repository.mapper")
public class TestConfiguration {

    @Bean
    public DataSource dataSource() {
        DataSource dataSource = DataSourceBuilder.create().url("jdbc:mysql://192.168.33.10:3306/aa?useUnicode=true&amp;characterEncoding=UTF-8").username("root").build();
        return dataSource;
    }

    @Bean
    public SqlSessionFactoryBean sqlSessionFactory() {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource());
        bean.setTypeAliasesPackage("com.tuotiansudai.repository.model");
        return bean;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }
}