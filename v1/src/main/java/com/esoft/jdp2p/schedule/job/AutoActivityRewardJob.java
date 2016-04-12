package com.esoft.jdp2p.schedule.job;

import com.esoft.archer.user.service.impl.JulyActivityRewardService;
import com.esoft.core.annotations.Logger;
import org.apache.commons.logging.Log;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AutoActivityRewardJob implements Job {

    @Logger
    Log log;

    @Autowired
    private JulyActivityRewardService julyActivityRewardService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            julyActivityRewardService.reward();
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
            throw e;
        }
    }
}
