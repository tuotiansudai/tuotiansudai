package com.tuotiansudai.client;

import com.tuotiansudai.dto.Environment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class MQWrapperClientConf {

    @Value("${common.environment}")
    private Environment env;

    @Bean
    public MQWrapperClient mqWrapperClient() {
        return isFormalEnv() ? new MQWrapperClientFormal() : new MQWrapperClientUT();
    }

    private boolean isFormalEnv() {
        return env != Environment.UT && env != Environment.DEV;
    }
}
