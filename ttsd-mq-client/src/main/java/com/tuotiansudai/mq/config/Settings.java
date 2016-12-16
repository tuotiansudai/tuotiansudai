package com.tuotiansudai.mq.config;

import com.tuotiansudai.mq.config.setting.AliyunMnsSetting;
import com.tuotiansudai.mq.tools.RedisClient;
import org.springframework.context.annotation.Bean;

public class Settings {

    @Bean
    public RedisClient redisClient() {
        return new RedisClient();
    }

    @Bean
    public AliyunMnsSetting aliyunMnsSetting() {
        return new AliyunMnsSetting();
    }
}
