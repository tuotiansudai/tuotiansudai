package com.tuotiansudai.mq.config;

import com.aliyun.mns.client.CloudAccount;
import com.aliyun.mns.client.MNSClient;
import com.tuotiansudai.mq.client.MQConsumer;
import com.tuotiansudai.mq.client.impl.MQConsumerAliyunMNS;
import com.tuotiansudai.mq.client.impl.MQConsumerRedis;
import com.tuotiansudai.mq.config.setting.AliyunMnsSetting;
import com.tuotiansudai.mq.config.setting.MessageConsumerSetting;
import com.tuotiansudai.mq.consumer.MessageConsumerFactory;
import com.tuotiansudai.mq.tools.RedisClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@Import(Settings.class)
public class MQConsumerConfig {
    @Value("${aliyun.mns.enabled}")
    private boolean enableAliyunMNS;

    @Bean
    public MessageConsumerSetting messageConsumerSetting() {
        return new MessageConsumerSetting();
    }

    @Bean
    public MessageConsumerFactory messageConsumerFactory(RedisClient redisClient, AliyunMnsSetting aliyunMnsSetting, MessageConsumerSetting consumerSetting) {
        return new MessageConsumerFactory(mqConsumer(redisClient, aliyunMnsSetting, consumerSetting));
    }

    private MQConsumer mqConsumer(RedisClient redisClient, AliyunMnsSetting aliyunMnsSetting, MessageConsumerSetting consumerSetting) {
        if (enableAliyunMNS) {
            return new MQConsumerAliyunMNS(mnsClient(aliyunMnsSetting), redisClient, consumerSetting);
        } else {
            return new MQConsumerRedis(redisClient, consumerSetting);
        }
    }

    private MNSClient mnsClient(AliyunMnsSetting setting) {
        CloudAccount account = new CloudAccount(
                setting.getAccessKeyId(),
                setting.getAccessKeySecret(),
                setting.getEndpoint()
        );
        return account.getMNSClient();
    }
}
