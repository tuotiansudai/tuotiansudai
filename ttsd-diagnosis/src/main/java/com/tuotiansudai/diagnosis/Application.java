package com.tuotiansudai.diagnosis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication(exclude = {FreeMarkerAutoConfiguration.class})
@PropertySource(value = {"classpath:ttsd-env.properties"})
public class Application {
    public static void main(String[] args) {
        SpringApplication
                .run(Application.class)
                .getBean(DiagnosisApplication.class)
                .start(args);
    }
}
