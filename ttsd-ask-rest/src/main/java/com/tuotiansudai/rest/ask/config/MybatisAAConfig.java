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
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
public class MybatisAAConfig {
    @Bean
    public AAConnectionConfig aaConnectionConfig() {
        return new AAConnectionConfig();
    }

    private HikariConfig hikariCPConfig(AAConnectionConfig connConfig) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(String.format("jdbc:mysql://%s:%s/%s?useUnicode=true&characterEncoding=UTF-8&useSSL=false",
                connConfig.getDbHost(), connConfig.getDbPort(), connConfig.getDbName()));
        config.setUsername(connConfig.getDbUser());
        config.setPassword(connConfig.getDbPassword());
        config.setDriverClassName("com.mysql.jdbc.Driver");
        config.setMinimumIdle(1);
        config.setMaximumPoolSize(5);
        return config;
    }

    @Bean
    public DataSource aaHikariCPDataSource(@Qualifier("aaConnectionConfig") AAConnectionConfig aaConnectionConfig) {
        return new HikariDataSource(hikariCPConfig(aaConnectionConfig));
    }

    @Bean
    public MapperScannerConfigurer aaMapperScannerConfigurer() {
        MapperScannerConfigurer configurer = new MapperScannerConfigurer();
        configurer.setBasePackage("com.tuotiansudai.repository.mapper");
        configurer.setSqlSessionFactoryBeanName("aaSqlSessionFactory");
        return configurer;
    }

    @Bean
    public DataSourceTransactionManager aaTransactionManager(@Qualifier("aaHikariCPDataSource") DataSource aaHikariCPDataSource) {
        return new DataSourceTransactionManager(aaHikariCPDataSource);
    }

    @Bean
    public SqlSessionFactory aaSqlSessionFactory(@Qualifier("aaHikariCPDataSource") DataSource aaHikariCPDataSource) throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(aaHikariCPDataSource);
        sessionFactory.setTypeAliasesPackage("com.tuotiansudai.repository.model");
        return sessionFactory.getObject();
    }

    public static class AAConnectionConfig {
        @Value("${common.jdbc.host}")
        private String dbHost;
        @Value("${common.jdbc.port}")
        private String dbPort;
        @Value("${common.jdbc.username}")
        private String dbUser;
        @Value("${common.jdbc.password}")
        private String dbPassword;
        private String dbName = "aa";

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
