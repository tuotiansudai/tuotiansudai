package com.tuotiansudai.diagnosis;

import com.tuotiansudai.spring.ETCDPropertyPlaceholderConfigurer;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(exclude = {FreeMarkerAutoConfiguration.class})
public class Application {
    public static void main(String[] args) {
        SpringApplication
                .run(Application.class)
                .getBean(DiagnosisApplication.class)
                .start(args);
    }

    @Bean
    public PropertyPlaceholderConfigurer propertyOverrideConfigurer() {
        return new ETCDPropertyPlaceholderConfigurer();
    }
}
