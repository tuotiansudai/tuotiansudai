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
@MapperScan(basePackages = "com.tuotiansudai.fudian.mapper.fudian", sqlSessionTemplateRef = "fudianSqlSessionTemplate")
public class MybatisFudianConfig {

    @Primary
    @Bean(name = "fudianDatasource")
    @ConfigurationProperties(prefix = "spring.datasource.fudian")
    public DataSource fudianDatasource(){
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name = "fudianSqlSessionFactory")
    public SqlSessionFactory fudianSqlSessionFactory(@Qualifier("fudianDatasource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        return bean.getObject();
    }

    @Primary
    @Bean(name = "fudianTransactionManager")
    public DataSourceTransactionManager fudianTransactionManager(@Qualifier("fudianDatasource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Primary
    @Bean(name = "fudianSqlSessionTemplate")
    public SqlSessionTemplate fudianSqlSessionTemplate(@Qualifier("fudianSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
