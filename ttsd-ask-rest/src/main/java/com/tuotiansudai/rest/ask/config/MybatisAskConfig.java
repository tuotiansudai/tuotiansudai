package com.tuotiansudai.rest.ask.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
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
public class MybatisAskConfig {
    @Bean
    public ConnectionConfig connectionConfig() {
        return new ConnectionConfig();
    }

    private HikariConfig hikariCPConfig(ConnectionConfig connConfig) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(String.format("jdbc:mysql://%s:%s/%s?useUnicode=true&characterEncoding=UTF-8&useSSL=false",
                connConfig.getDbHost(), connConfig.getDbPort(), connConfig.getDbName()));
        config.setUsername(connConfig.getDbUser());
        config.setPassword(connConfig.getDbPassword());
        config.setDriverClassName("com.mysql.jdbc.Driver");
        config.setMinimumIdle(1);
        config.setMaximumPoolSize(5);
        config.setConnectionInitSql("set names utf8mb4");
        return config;
    }

    @Primary
    @Bean
    public DataSource askHikariCPDataSource(@Qualifier("connectionConfig") ConnectionConfig connectionConfig) {
        return new HikariDataSource(hikariCPConfig(connectionConfig));
    }

    @Bean
    public MapperScannerConfigurer askMapperScannerConfigurer() {
        MapperScannerConfigurer configurer = new MapperScannerConfigurer();
        configurer.setBasePackage("com.tuotiansudai.ask.repository.mapper");
        configurer.setSqlSessionFactoryBeanName("askSqlSessionFactory");
        return configurer;
    }

    @Bean
    public DataSourceTransactionManager askTransactionManager(@Qualifier("askHikariCPDataSource") DataSource askHikariCPDataSource) {
        return new DataSourceTransactionManager(askHikariCPDataSource);
    }

    @Primary
    @Bean
    public SqlSessionFactory askSqlSessionFactory(@Qualifier("askHikariCPDataSource") DataSource askHikariCPDataSource) throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(askHikariCPDataSource);
        sessionFactory.setTypeAliasesPackage("com.tuotiansudai.ask.repository.model");
        return sessionFactory.getObject();
    }

    public static class ConnectionConfig {
        @Value("${common.jdbc.host}")
        private String dbHost;
        @Value("${common.jdbc.port}")
        private String dbPort;
        @Value("${ask.jdbc.username}")
        private String dbUser;
        @Value("${ask.jdbc.password}")
        private String dbPassword;
        @Value("${ask.jdbc.schema}")
        private String dbName;

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

        public String getDbName() {
            return dbName;
        }

        public void setDbName(String dbName) {
            this.dbName = dbName;
        }
    }

}
