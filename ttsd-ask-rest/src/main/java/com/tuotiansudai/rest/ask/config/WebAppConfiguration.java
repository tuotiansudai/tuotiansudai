package com.tuotiansudai.rest.ask.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;


@Configuration
@PropertySource(
        ignoreResourceNotFound = true, value = {
        "classpath:ttsd-env.properties",
        "classpath:ttsd-biz.properties"
})
public class WebAppConfiguration {
}
