package com.tuotiansudai.console.activity.config;

import com.tuotiansudai.spring.session.MyDefaultCookieSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.session.web.http.CookieSerializer;

@Configuration
public class SessionConfig {

    @Value("${common.redis.host}")
    private String redisHost;

    @Value("${common.redis.port}")
    private int redisPort;

    @Value("${common.redis.password}")
    private String redisPassword;

    @Value("${common.session.db}")
    protected int redisSessionDb;

    @Bean
    public CookieSerializer cookieSerializer() {
        return new MyDefaultCookieSerializer();
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        JedisConnectionFactory factory = new JedisConnectionFactory();
        factory.setHostName(redisHost);
        factory.setPort(redisPort);
        factory.setDatabase(redisSessionDb);
        factory.setPassword(redisPassword);
        return factory;
    }
}
