package com.tuotiansudai.borrow.config;

import com.tuotiansudai.etcd.ETCDPropertySourcesPlaceholderConfigurer;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = {"com.tuotiansudai.repository.mapper"})
@EnableTransactionManagement
public class AppConfig {

    @Bean
    public ETCDPropertySourcesPlaceholderConfigurer placeholderConfigurer(){
        return new ETCDPropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public RedissonClient redissonClient(@Autowired RedissonConfig redissonConfig) {
        Config config = new Config();
        config.useSingleServer()
                .setAddress(redissonConfig.getAddress())
                .setDatabase(redissonConfig.getDatabase())
                .setTimeout(redissonConfig.getTimeout())
                .setConnectionPoolSize(redissonConfig.getConnectionPoolSize());
        return Redisson.create(config);
    }
}
