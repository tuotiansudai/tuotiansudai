package com.tuotiansudai.borrow.wrapper;

import com.tuotiansudai.mq.config.MQConsumerConfig;
import com.tuotiansudai.mq.consumer.MessageConsumerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Import;

@SpringBootApplication()
@Import({MQConsumerConfig.class})
public class Application extends SpringBootServletInitializer{

    public static void main(String[] args) {
        SpringApplication
                .run(Application.class, args)
                .getBean(MessageConsumerFactory.class)
                .start();
    }
}
