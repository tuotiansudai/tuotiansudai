package com.tuotiansudai.console.activity.config;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;
import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
public class MybatisActivityConfig {

    @Bean
    @Primary
    @ConfigurationProperties(prefix="spring.datasource.activity")
    public DataSource hikariCPActivityDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean
    public MapperScannerConfigurer activityMapperScannerConfigurer() {
        MapperScannerConfigurer configurer = new MapperScannerConfigurer();
        configurer.setBasePackage("com.tuotiansudai.activity.repository.mapper");
        configurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
        return configurer;
    }

    @Bean
    public DataSourceTransactionManager transactionManager(@Qualifier("hikariCPActivityDataSource") DataSource hikariCPActivityDataSource) {
        return new DataSourceTransactionManager(hikariCPActivityDataSource);
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(@Qualifier("hikariCPActivityDataSource") DataSource hikariCPActivityDataSource) throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(hikariCPActivityDataSource);
        sessionFactory.setTypeAliasesPackage("com.tuotiansudai.activity.repository.model");
        return sessionFactory.getObject();
    }
}
