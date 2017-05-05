package com.tuotiansudai.console.activity.config;

import com.squareup.okhttp.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class ClientConfig {

    @Bean
    public OkHttpClient okHttpClient() throws Exception {
        return new OkHttpClient();
    }
}
