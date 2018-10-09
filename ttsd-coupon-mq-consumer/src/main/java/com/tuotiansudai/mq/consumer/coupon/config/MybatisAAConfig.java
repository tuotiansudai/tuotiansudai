package com.tuotiansudai.mq.consumer.coupon.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
public class MybatisAAConfig {
    @Bean
    public MybatisAAConnectionConfig mybatisAAConnectionConfig() {
        return new MybatisAAConnectionConfig();
    }

    @Bean(name = "hikariCPAAConfig")
    public HikariConfig hikariCPAAConfig(MybatisAAConnectionConfig connConfig) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(String.format("jdbc:mysql://%s:%s/aa?useUnicode=true&characterEncoding=UTF-8",
                connConfig.getDbHost(), connConfig.getDbPort()));
        config.setUsername(connConfig.getDbUser());
        config.setPassword(connConfig.getDbPassword());
        config.setDriverClassName("com.mysql.jdbc.Driver");
        config.setMinimumIdle(1);
        config.setMaximumPoolSize(5);
        return config;
    }

    @Primary
    @Bean
    public DataSource hikariCPAADataSource(@Autowired @Qualifier("hikariCPAAConfig") HikariConfig hikariConfig) {
        return new HikariDataSource(hikariConfig);
    }

    @Bean
    public MapperScannerConfigurer aaMapperScannerConfigurer() {
        MapperScannerConfigurer configurer = new MapperScannerConfigurer();
        configurer.setBasePackage("com.tuotiansudai.repository.mapper,com.tuotiansudai.membership.repository.mapper");
        configurer.setSqlSessionFactoryBeanName("aaSqlSessionFactory");
        return configurer;
    }

    @Primary
    @Bean
    public DataSourceTransactionManager aaTransactionManager(@Qualifier("hikariCPAADataSource") DataSource hikariCPAADataSource) {
        return new DataSourceTransactionManager(hikariCPAADataSource);
    }

    @Primary
    @Bean
    public SqlSessionFactory aaSqlSessionFactory(@Qualifier("hikariCPAADataSource") DataSource hikariCPAADataSource) throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(hikariCPAADataSource);
        sessionFactory.setTypeAliasesPackage("com.tuotiansudai.repository.model,com.tuotiansudai.membership.repository.model");
        return sessionFactory.getObject();
    }

    public static class MybatisAAConnectionConfig {
        @Value("${common.jdbc.host}")
        private String dbHost;
        @Value("${common.jdbc.port}")
        private String dbPort;
        @Value("${common.jdbc.username}")
        private String dbUser;
        @Value("${common.jdbc.password}")
        private String dbPassword;

        public String getDbHost() {
            return dbHost;
        }

        public void setDbHost(String dbHost) {
            this.dbHost = dbHost;
        }

        public String getDbPort() {
            return dbPort;
        }

        public void setDbPort(String dbPort) {
            this.dbPort = dbPort;
        }

        public String getDbUser() {
            return dbUser;
        }

        public void setDbUser(String dbUser) {
            this.dbUser = dbUser;
        }

        public String getDbPassword() {
            return dbPassword;
        }

        public void setDbPassword(String dbPassword) {
            this.dbPassword = dbPassword;
        }
    }

}
