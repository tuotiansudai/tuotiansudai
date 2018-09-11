package com.tuotiansudai.mq.consumer.auditLog.config;

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
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
public class MybatisLogConfig {
    @Bean
    public MybatisLogConnectionConfig mybatisLogConnectionConfig() {
        return new MybatisLogConnectionConfig();
    }

    @Bean(name = "hikariCPLogConfig")
    public HikariConfig hikariCPLogConfig(MybatisLogConnectionConfig connConfig) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(String.format("jdbc:mysql://%s:%s/edxlog?useUnicode=true&characterEncoding=UTF-8&useSSL=false",
                connConfig.getDbHost(), connConfig.getDbPort()));
        config.setUsername(connConfig.getDbUser());
        config.setPassword(connConfig.getDbPassword());
        config.setDriverClassName("com.mysql.jdbc.Driver");
        config.setMinimumIdle(1);
        config.setMaximumPoolSize(5);
        return config;
    }

    @Bean
    public DataSource hikariCPLogDataSource(@Autowired @Qualifier("hikariCPLogConfig") HikariConfig hikariConfig) {
        return new HikariDataSource(hikariConfig);
    }

    @Bean
    public MapperScannerConfigurer logMapperScannerConfigurer() {
        MapperScannerConfigurer configurer = new MapperScannerConfigurer();
        configurer.setBasePackage("com.tuotiansudai.log.repository.mapper");
        configurer.setSqlSessionFactoryBeanName("logSqlSessionFactory");
        return configurer;
    }

    @Bean
    public DataSourceTransactionManager logTransactionManager(@Qualifier("hikariCPLogDataSource") DataSource hikariCPLogDataSource) {
        return new DataSourceTransactionManager(hikariCPLogDataSource);
    }

    @Bean
    public SqlSessionFactory logSqlSessionFactory(@Qualifier("hikariCPLogDataSource") DataSource hikariCPLogDataSource) throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(hikariCPLogDataSource);
        sessionFactory.setTypeAliasesPackage("com.tuotiansudai.log.repository.model");
        return sessionFactory.getObject();
    }

    public static class MybatisLogConnectionConfig {
        @Value("${common.jdbc.host}")
        private String dbHost;
        @Value("${common.jdbc.port}")
        private String dbPort;
        @Value("${log.jdbc.username}")
        private String dbUser;
        @Value("${log.jdbc.password}")
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
