package com.tuotiansudai.console.activity.config;

import com.squareup.okhttp.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class ClientConfig {

    @Value("${console.jedis.pool.maxTotal}")
    private int maxTotal;

    @Value("${common.jedis.pool.maxWaitMillis}")
    private int maxWaitMillis;

    @Bean
    public JedisPoolConfig jedisPoolConfig() throws Exception {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(maxTotal);
        poolConfig.setMaxWaitMillis(maxWaitMillis);
        return poolConfig;
    }

    @Bean
    public OkHttpClient okHttpClient() throws Exception {
        return new OkHttpClient();
    }
}
