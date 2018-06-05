package com.tuotiansudai.mq.consumer.loan.config;

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
public class MybatisAnxinConfig {
    @Bean
    public MybatisAnxinConnectionConfig mybatisAnxinConnectionConfig() {
        return new MybatisAnxinConnectionConfig();
    }

    @Bean(name = "hikariCPAnxinConfig")
    public HikariConfig hikariCPAnxinConfig(MybatisAnxinConnectionConfig connConfig) {
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
    public DataSource hikariCPAnxinDataSource(@Autowired @Qualifier("hikariCPAnxinConfig") HikariConfig hikariConfig) {
        return new HikariDataSource(hikariConfig);
    }

    @Bean
    public MapperScannerConfigurer anxinMapperScannerConfigurer() {
        MapperScannerConfigurer configurer = new MapperScannerConfigurer();
        configurer.setBasePackage("com.tuotiansudai.cfca.mapper");
        configurer.setSqlSessionFactoryBeanName("anxinSqlSessionFactory");
        return configurer;
    }

    @Bean
    public DataSourceTransactionManager anxinTransactionManager(@Qualifier("hikariCPAnxinDataSource") DataSource hikariCPAnxinDataSource) {
        return new DataSourceTransactionManager(hikariCPAnxinDataSource);
    }

    @Bean
    public SqlSessionFactory anxinSqlSessionFactory(@Qualifier("hikariCPAnxinDataSource") DataSource hikariCPAnxinDataSource) throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(hikariCPAnxinDataSource);
        sessionFactory.setTypeAliasesPackage("com.tuotiansudai.cfca.model");
        return sessionFactory.getObject();
    }

    public static class MybatisAnxinConnectionConfig {
        @Value("${common.jdbc.host}")
        private String dbHost;
        @Value("${common.jdbc.port}")
        private String dbPort;
        @Value("${anxin.jdbc.username}")
        private String dbUser;
        @Value("${anxin.jdbc.password}")
        private String dbPassword;
        @Value("${anxin.jdbc.schema}")
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
