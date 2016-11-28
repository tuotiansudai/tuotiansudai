package com.tuotiansudai.console.activity.config;

import com.tuotiansudai.spring.session.MyDefaultCookieSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.web.http.CookieSerializer;

@Configuration
public class SessionConfig {

    @Bean
    public CookieSerializer cookieSerializer() {
        return new MyDefaultCookieSerializer();
    }
}
