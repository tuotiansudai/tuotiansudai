package com.tuotiansudai.job;

import com.tuotiansudai.service.UserService;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AutoReFleshAreaByMobileJob implements Job {


    @Autowired
    private UserService userService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        userService.reFleshAreaByMobileInJob();
    }
}
