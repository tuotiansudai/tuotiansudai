package com.tuotiansudai.mq.config;

import com.aliyun.mns.client.CloudAccount;
import com.aliyun.mns.client.MNSClient;
import com.tuotiansudai.mq.client.MQProducer;
import com.tuotiansudai.mq.client.impl.MQProducerAliyunMNS;
import com.tuotiansudai.mq.client.impl.MQProducerRedis;
import com.tuotiansudai.mq.config.setting.AliyunMnsSetting;
import com.tuotiansudai.mq.tools.RedisClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@Import(Settings.class)
public class MQProducerConfig {

    @Value("${aliyun.mns.enabled}")
    private boolean enableAliyumMNS;

    @Bean
    public MQProducer mqProducer(RedisClient redisClient, AliyunMnsSetting aliyunMnsSetting) {
        if (enableAliyumMNS) {
            return new MQProducerAliyunMNS(mnsClient(aliyunMnsSetting));
        } else {
            return new MQProducerRedis(redisClient);
        }
    }

    private MNSClient mnsClient(AliyunMnsSetting setting) {
        CloudAccount account = new CloudAccount(
                setting.getAccessKeyId(),
                setting.getAccessKeySecret(),
                setting.getEndpoint());
        return account.getMNSClient();
    }
}
