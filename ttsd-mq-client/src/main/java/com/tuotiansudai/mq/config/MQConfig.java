package com.tuotiansudai.mq.config;

import com.aliyun.mns.client.CloudAccount;
import com.aliyun.mns.client.MNSClient;
import com.tuotiansudai.dto.Environment;
import com.tuotiansudai.mq.client.MQClient;
import com.tuotiansudai.mq.client.impl.MQClientAliyumMNS;
import com.tuotiansudai.mq.client.impl.MQClientLocalMock;
import com.tuotiansudai.mq.consumer.MessageConsumerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

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
    private boolean enableAliyumMNS;

    @Value("${common.environment}")
    public void setEnvironment(Environment environment) {
        this.enableAliyumMNS = Arrays.asList(Environment.PRODUCTION, Environment.QA).contains(environment);
    }

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
    public MQClient mqClient(MNSClient mnsClient) {
        if (enableAliyumMNS) {
            return new MQClientAliyumMNS(mnsClient);
        } else {
            return new MQClientLocalMock();
        }
    }

    @Bean
    public MessageConsumerFactory messageConsumerFactory() {
        return new MessageConsumerFactory();
    }
}
