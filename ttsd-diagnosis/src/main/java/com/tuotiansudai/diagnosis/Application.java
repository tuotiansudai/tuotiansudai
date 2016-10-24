package com.tuotiansudai.diagnosis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;

@SpringBootApplication
@EnableAutoConfiguration(exclude = {FreeMarkerAutoConfiguration.class})
public class Application {
    public static void main(String[] args) {
        SpringApplication
                .run(Application.class)
                .getBean(DiagnosisApplication.class)
                .start(args);
    }
}
