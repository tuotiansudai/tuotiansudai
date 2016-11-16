package com.tuotiansudai.mq.config;

import com.aliyun.mns.client.CloudAccount;
import com.aliyun.mns.client.MNSClient;
import com.tuotiansudai.mq.client.MQClient;
import com.tuotiansudai.mq.consumer.MessageConsumerFactory;
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

    @Bean
    public AliyunMnsConfig aliyunMnsConfig() {
        return new AliyunMnsConfig(accessKeyId, accessKeySecret, endpoint);
    }

    @Bean
    public MNSClient mnsClient(AliyunMnsConfig aliyunMnsConfig) {
        CloudAccount account = new CloudAccount(
                aliyunMnsConfig.getAccessKeyId(),
                aliyunMnsConfig.getAccessKeySecret(),
                aliyunMnsConfig.getEndpoint());
        return account.getMNSClient();
    }

    @Bean
    public MQClient mqClient(MNSClient mnsClient) {
        return new MQClient(mnsClient);
    }

    @Bean
    public MessageConsumerFactory messageConsumerFactory() {
        return new MessageConsumerFactory();
    }
}
