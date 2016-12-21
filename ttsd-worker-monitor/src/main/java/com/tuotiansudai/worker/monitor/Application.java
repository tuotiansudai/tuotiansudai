package com.tuotiansudai.worker.monitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {FreeMarkerAutoConfiguration.class, DataSourceAutoConfiguration.class})
public class Application {
    public static void main(String[] args) {
        SpringApplication
                .run(Application.class, args)
                .getBean(WorkerMonitor.class)
                .start();
    }
}
