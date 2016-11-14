package com.tuotian.mq.consumer;

import com.tuotiansudai.mq.config.MQConfig;
import com.tuotiansudai.mq.consumer.MessageConsumerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.context.annotation.Import;

@SpringBootApplication(exclude = {FreeMarkerAutoConfiguration.class})
@Import(MQConfig.class)
public class Application {
    public static void main(String[] args) {
        SpringApplication
                .run(Application.class, args)
                .getBean(MessageConsumerFactory.class)
                .start();
    }
}
