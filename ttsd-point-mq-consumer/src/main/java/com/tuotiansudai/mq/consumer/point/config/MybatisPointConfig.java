package com.tuotiansudai.mq.consumer.point.config;

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
public class MybatisPointConfig {
    @Bean
    public MybatisPointConnectionConfig mybatisPointConnectionConfig() {
        return new MybatisPointConnectionConfig();
    }

    @Bean(name = "hikariCPPointConfig")
    public HikariConfig hikariCPPointConfig(MybatisPointConnectionConfig connConfig) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(String.format("jdbc:mysql://%s:%s/edxpoint?useUnicode=true&characterEncoding=UTF-8&useSSL=false",
                connConfig.getDbHost(), connConfig.getDbPort()));
        config.setUsername(connConfig.getDbUser());
        config.setPassword(connConfig.getDbPassword());
        config.setDriverClassName("com.mysql.jdbc.Driver");
        config.setMinimumIdle(1);
        config.setMaximumPoolSize(5);
        return config;
    }

    @Primary
    @Bean(name = "hikariCPPointDataSource")
    public DataSource hikariCPPointDataSource(@Autowired @Qualifier("hikariCPPointConfig") HikariConfig hikariConfig) {
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
    public DataSourceTransactionManager pointTransactionManager(@Qualifier("hikariCPPointDataSource") DataSource hikariCPPointDataSource) {
        return new DataSourceTransactionManager(hikariCPPointDataSource);
    }

    @Primary
    @Bean(name = "pointSqlSessionFactory")
    public SqlSessionFactory pointSqlSessionFactory(@Qualifier("hikariCPPointDataSource") DataSource hikariCPPointDataSource) throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(hikariCPPointDataSource);
        sessionFactory.setTypeAliasesPackage("com.tuotiansudai.point.repository.model");
        return sessionFactory.getObject();
    }

    public static class MybatisPointConnectionConfig {
        @Value("${common.jdbc.host}")
        private String dbHost;
        @Value("${common.jdbc.port}")
        private String dbPort;
        @Value("${point.jdbc.username}")
        private String dbUser;
        @Value("${point.jdbc.password}")
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
