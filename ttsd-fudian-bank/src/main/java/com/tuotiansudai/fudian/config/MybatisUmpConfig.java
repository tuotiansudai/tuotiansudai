package com.tuotiansudai.fudian.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "com.tuotiansudai.fudian.mapper.ump")
public class MybatisUmpConfig {

    @Primary
    @Bean(name = "umpDatasource")
    @ConfigurationProperties(prefix = "spring.ump-datasource")
    public DataSource fudianDatasource(){
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name = "umpSqlSessionFactory")
    public SqlSessionFactory umpSqlSessionFactory(@Qualifier("umpDatasource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        return bean.getObject();
    }

    @Bean(name = "umpTransactionManager")
    @Primary
    public DataSourceTransactionManager umpTransactionManager(@Qualifier("umpDatasource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "umpSqlSessionTemplate")
    @Primary
    public SqlSessionTemplate umpSqlSessionTemplate(@Qualifier("umpSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
