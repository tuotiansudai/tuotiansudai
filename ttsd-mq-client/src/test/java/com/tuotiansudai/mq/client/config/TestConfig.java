package com.tuotiansudai.mq.client.config;

import com.tuotiansudai.mq.config.MQConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(MQConfig.class)
public class TestConfig {
}
