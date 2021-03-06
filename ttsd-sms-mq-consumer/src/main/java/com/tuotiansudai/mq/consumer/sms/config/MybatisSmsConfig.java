package com.tuotiansudai.mq.consumer.sms.config;

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
public class MybatisSmsConfig {
    @Bean
    public MybatisSmsConnectionConfig mybatisSmsConnectionConfig() {
        return new MybatisSmsConnectionConfig();
    }

    @Bean(name = "hikariCPSmsConfig")
    public HikariConfig hikariCPSmsConfig(MybatisSmsConnectionConfig connConfig) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(String.format("jdbc:mysql://%s:%s/sms_operations?useUnicode=true&characterEncoding=UTF-8",
                connConfig.getDbHost(), connConfig.getDbPort()));
        config.setUsername(connConfig.getDbUser());
        config.setPassword(connConfig.getDbPassword());
        config.setDriverClassName("com.mysql.jdbc.Driver");
        config.setMinimumIdle(1);
        config.setMaximumPoolSize(5);
        return config;
    }

    @Bean
    public DataSource hikariCPSmsDataSource(@Autowired @Qualifier("hikariCPSmsConfig") HikariConfig hikariConfig) {
        return new HikariDataSource(hikariConfig);
    }

    @Bean
    public MapperScannerConfigurer smsMapperScannerConfigurer() {
        MapperScannerConfigurer configurer = new MapperScannerConfigurer();
        configurer.setBasePackage("com.tuotiansudai.mq.consumer.sms.repository.mapper");
        configurer.setSqlSessionFactoryBeanName("smsSqlSessionFactory");
        return configurer;
    }

    @Bean
    public DataSourceTransactionManager smsTransactionManager(@Qualifier("hikariCPSmsDataSource") DataSource hikariCPSmsDataSource) {
        return new DataSourceTransactionManager(hikariCPSmsDataSource);
    }

    @Bean
    public SqlSessionFactory smsSqlSessionFactory(@Qualifier("hikariCPSmsDataSource") DataSource hikariCPSmsDataSource) throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(hikariCPSmsDataSource);
        sessionFactory.setTypeAliasesPackage("com.tuotiansudai.mq.consumer.sms.repository.model");
        return sessionFactory.getObject();
    }

    public static class MybatisSmsConnectionConfig {
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
