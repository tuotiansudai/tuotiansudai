package com.tuotiansudai.mq.consumer.point.config;

import com.tuotiansudai.point.service.PointBillService;
import com.tuotiansudai.point.service.PointTaskService;
import com.tuotiansudai.point.service.impl.PointBillServiceImpl;
import com.tuotiansudai.point.service.impl.PointTaskServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConsumerConfiguration {

    @Bean
    public PointBillService pointBillService(){
        return new PointBillServiceImpl();
    }

    @Bean
    public PointTaskService pointTaskService(){
        return new PointTaskServiceImpl();
    }
}
