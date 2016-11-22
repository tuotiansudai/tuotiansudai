package com.tuotiansudai.diagnosis.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
public class MybatisAAReadOnlyConfig {

    @Bean(name = "hikariCPAAConfig")
    @ConfigurationProperties(prefix = "spring.datasource.aaro")
    public HikariConfig hikariCPAAConfig() {
        return new HikariConfig();
    }

    @Bean
    @Primary
    public DataSource aaDataSource(@Qualifier("hikariCPAAConfig") HikariConfig hikariCPAAConfig) {
        return new HikariDataSource(hikariCPAAConfig);
    }

    @Bean
    public DataSourceTransactionManager transactionManager(DataSource aaDataSource) {
        return new DataSourceTransactionManager(aaDataSource);
    }

    @Bean
    public MapperScannerConfigurer aaMapperScannerConfigurer() {
        MapperScannerConfigurer configurer = new MapperScannerConfigurer();
        configurer.setBasePackage("com.tuotiansudai.repository.mapper," +
                "com.tuotiansudai.transfer.repository.mapper," +
                "com.tuotiansudai.membership.repository.mapper," +
                "com.tuotiansudai.coupon.repository.mapper," +
                "com.tuotiansudai.activity.repository.autumn.mapper");
        configurer.setSqlSessionFactoryBeanName("aaSqlSessionFactory");
        return configurer;
    }

    @Bean
    public SqlSessionFactory aaSqlSessionFactory(@Qualifier("aaDataSource") DataSource aaDataSource) throws Exception {
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setCacheEnabled(false);

        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setConfiguration(configuration);
        sessionFactory.setDataSource(aaDataSource);
        sessionFactory.setTypeAliasesPackage("com.tuotiansudai.repository.model," +
                "com.tuotiansudai.transfer.repository.model," +
                "com.tuotiansudai.membership.repository.model," +
                "com.tuotiansudai.coupon.repository.model," +
                "com.tuotiansudai.activity.repository.autumn.model");
        return sessionFactory.getObject();
    }
}
