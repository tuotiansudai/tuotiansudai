package com.esoft.jdp2p.schedule.job;

import com.esoft.archer.user.exception.UserNotFoundException;
import com.esoft.archer.user.service.UserService;
import com.esoft.core.annotations.Logger;
import com.esoft.core.util.SpringBeanUtil;
import org.apache.commons.logging.Log;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class RegisterEmailVerificationJob implements Job {
    public static final String USER_ID = "userId";
    public static final String AUTH_CODE = "authCode";
    public static final String URL = "url";

    @Logger
    static Log log;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        UserService userService = (UserService) SpringBeanUtil.getBeanByName("userService");
        String userId = jobExecutionContext.getJobDetail().getJobDataMap().getString(USER_ID);
        String authCode = jobExecutionContext.getJobDetail().getJobDataMap().getString(AUTH_CODE);
        String url = jobExecutionContext.getJobDetail().getJobDataMap().getString(URL);
        try {
            userService.sendActiveEmail(userId, authCode, url);
        } catch (UserNotFoundException e) {
            log.error(e.getLocalizedMessage(), e);
        }
    }
}
