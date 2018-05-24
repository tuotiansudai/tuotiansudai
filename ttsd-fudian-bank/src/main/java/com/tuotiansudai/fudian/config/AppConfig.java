package com.tuotiansudai.fudian.config;

import com.tuotiansudai.etcd.ETCDPropertySourcesPlaceholderConfigurer;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public ETCDPropertySourcesPlaceholderConfigurer propertyConfigurer() {
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
