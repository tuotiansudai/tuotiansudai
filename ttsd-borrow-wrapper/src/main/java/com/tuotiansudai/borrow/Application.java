package com.tuotiansudai.borrow;

import com.tuotiansudai.mq.config.MQProducerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({MQProducerConfig.class})
public class Application {

    public static void main(String[] args) {
        SpringApplication
                .run(Application.class, args)
                .start();
    }
}
