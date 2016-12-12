package com.tuotiansudai.mq.config;

import com.aliyun.mns.client.CloudAccount;
import com.aliyun.mns.client.MNSClient;
import com.tuotiansudai.mq.client.MQClient;
import com.tuotiansudai.mq.client.impl.MQClientAliyunMNS;
import com.tuotiansudai.mq.client.impl.MQClientRedis;
import com.tuotiansudai.mq.consumer.MessageConsumerFactory;
import com.tuotiansudai.mq.tools.RedisClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {
        "com.tuotiansudai.mq.config",
        "com.tuotiansudai.mq.client",
        "com.tuotiansudai.mq.consumer"
})
public class MQConfig {
    @Value("${aliyun.mns.accessKeyId}")
    private String accessKeyId;
    @Value("${aliyun.mns.accessKeySecret}")
    private String accessKeySecret;
    @Value("${aliyun.mns.endpoint}")
    private String endpoint;
    @Value("${aliyun.mns.enabled}")
    private boolean enableAliyumMNS;

    @Value("${mq.message.pop.period.seconds}")
    private int messagePopPeriodSeconds;
    @Value("${mq.error.count.threshold}")
    private int errorCountThreshold;
    @Value("${mq.error.sleep.seconds}")
    private int errorSleepSeconds;

    @Bean
    public AliyunMnsConfig aliyunMnsConfig() {
        if (enableAliyumMNS) {
            return new AliyunMnsConfig(accessKeyId, accessKeySecret, endpoint);
        } else {
            return null;
        }
    }

    @Bean
    public MNSClient mnsClient(AliyunMnsConfig aliyunMnsConfig) {
        if (enableAliyumMNS) {
            CloudAccount account = new CloudAccount(
                    aliyunMnsConfig.getAccessKeyId(),
                    aliyunMnsConfig.getAccessKeySecret(),
                    aliyunMnsConfig.getEndpoint());
            return account.getMNSClient();
        } else {
            return null;
        }
    }

    @Bean
    public RedisClient redisClient() {
        return new RedisClient();
    }

    @Bean
    public MQClient mqClient(MNSClient mnsClient, RedisClient redisClient) {
        MQClient mqClient;
        if (enableAliyumMNS) {
            mqClient = new MQClientAliyunMNS(mnsClient, redisClient);
        } else {
            mqClient = new MQClientRedis(redisClient);
        }
        mqClient.setMessagePopPeriodSeconds(messagePopPeriodSeconds);
        mqClient.setErrorCountThreshold(errorCountThreshold);
        mqClient.setErrorSleepSeconds(errorSleepSeconds);
        return mqClient;
    }

    @Bean
    public MessageConsumerFactory messageConsumerFactory() {
        return new MessageConsumerFactory();
    }
}
