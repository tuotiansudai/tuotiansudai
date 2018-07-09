package com.tuotiansudai.fudian.config;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class MybatisConfig {

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.fudian-datasource")
    public DataSource fudianDatasource(){
        return DataSourceBuilder.create().build();
    }

    @Bean(value = "upmDatasource")
    @ConfigurationProperties(prefix = "spring.ump-datasource")
    public DataSource umpDatasource(){
        return DataSourceBuilder.create().build();
    }

    @Bean
    @Primary
    public JdbcTemplate fudianJdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean(name = "umpJdbcTemplate")
    public JdbcTemplate secondJdbcTemplate(@Qualifier("upmDatasource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
