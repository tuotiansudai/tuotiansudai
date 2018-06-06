package com.tuotiansudai.worker.monitor;

import com.tuotiansudai.etcd.ETCDPropertySourcesPlaceholderConfigurer;
import com.tuotiansudai.mq.config.MQConsumerConfig;
import com.tuotiansudai.mq.config.MQProducerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;


@SpringBootApplication(exclude = {FreeMarkerAutoConfiguration.class, DataSourceAutoConfiguration.class})
@Import({MQConsumerConfig.class, MQProducerConfig.class})
public class Application {
    public static void main(String[] args) {
        SpringApplication
                .run(Application.class, args)
                .getBean(WorkerMonitor.class)
                .start();
    }

    @Bean
    public ETCDPropertySourcesPlaceholderConfigurer propertyConfigurer() {
        return new ETCDPropertySourcesPlaceholderConfigurer();
    }
}
