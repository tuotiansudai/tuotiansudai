package com.tuotiansudai.scheduler;

import com.tuotiansudai.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
//        Worker worker = applicationContext.getBean(Worker.class);
//        worker.start();
        UserService userService = applicationContext.getBean(UserService.class);
        userService.refreshAreaByMobileInJob();
    }
}
