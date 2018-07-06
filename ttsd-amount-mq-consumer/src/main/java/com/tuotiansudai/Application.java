package com.tuotiansudai;

import com.tuotiansudai.mq.config.MQConsumerConfig;
import com.tuotiansudai.mq.config.MQProducerConfig;
import com.tuotiansudai.mq.consumer.MessageConsumerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.context.annotation.Import;

@SpringBootApplication(
        scanBasePackages = {
                "com.tuotiansudai.mq.consumer.amount",
                "com.tuotiansudai.repository.mapper",
                "com.tuotiansudai.rest.client.mapper"
        },
        exclude = {FreeMarkerAutoConfiguration.class})
@Import({MQConsumerConfig.class, MQProducerConfig.class})
public class Application {
    public static void main(String[] args) {
        SpringApplication
                .run(Application.class, args)
                .getBean(MessageConsumerFactory.class)
                .start();
    }
}
