package com.tuotiansudai.console.activity.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
public class MybatisPointConfig {

    @Bean(name = "hikariCPPointConfig")
    @ConfigurationProperties(prefix = "spring.datasource.point")
    public HikariConfig hikariCPPointConfig() {
        return new HikariConfig();
    }

    @Bean
    public DataSource hikariCPPointDataSource(
            @Autowired @Qualifier("hikariCPPointConfig") HikariConfig hikariConfig) {
        return new HikariDataSource(hikariConfig);
    }

    @Bean
    public MapperScannerConfigurer pointMapperScannerConfigurer() {
        MapperScannerConfigurer configurer = new MapperScannerConfigurer();
        configurer.setBasePackage("com.tuotiansudai.point.repository.mapper");
        configurer.setSqlSessionFactoryBeanName("pointSqlSessionFactory");
        return configurer;
    }

    @Bean
    public DataSourceTransactionManager pointTransactionManager(@Qualifier("hikariCPPointDataSource") DataSource hikariCPActivityDataSource) {
        return new DataSourceTransactionManager(hikariCPActivityDataSource);
    }

    @Bean
    public SqlSessionFactory pointSqlSessionFactory(@Qualifier("hikariCPPointDataSource") DataSource hikariCPActivityDataSource) throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(hikariCPActivityDataSource);
        sessionFactory.setTypeAliasesPackage("com.tuotiansudai.point.repository.model");
        return sessionFactory.getObject();
    }
}
