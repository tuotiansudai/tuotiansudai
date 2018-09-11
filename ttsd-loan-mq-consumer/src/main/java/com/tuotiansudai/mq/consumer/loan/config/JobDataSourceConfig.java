package com.tuotiansudai.mq.consumer.loan.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class JobDataSourceConfig {
    @Bean
    public JobConnectionConfig jobConnectionConfig() {
        return new JobConnectionConfig();
    }

    @Bean
    public HikariConfig hikariCPJobConfig(JobConnectionConfig connConfig) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(String.format("jdbc:mysql://%s:%s/%s?useUnicode=true&characterEncoding=UTF-8&useSSL=false",
                connConfig.getDbHost(), connConfig.getDbPort(), connConfig.getDbName()));
        config.setUsername(connConfig.getDbUser());
        config.setPassword(connConfig.getDbPassword());
        config.setDriverClassName("com.mysql.jdbc.Driver");
        config.setMinimumIdle(1);
        config.setMaximumPoolSize(1);
        return config;
    }

    @Bean
    public DataSource hikariCPJobDataSource(@Qualifier("hikariCPJobConfig") HikariConfig hikariCPJobConfig) {
        return new HikariDataSource(hikariCPJobConfig);
    }

    public static class JobConnectionConfig {
        @Value("${common.jdbc.host}")
        private String dbHost;
        @Value("${common.jdbc.port}")
        private String dbPort;
        @Value("${common.jdbc.username}")
        private String dbUser;
        @Value("${common.jdbc.password}")
        private String dbPassword;
        private String dbName = "job_worker";

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
