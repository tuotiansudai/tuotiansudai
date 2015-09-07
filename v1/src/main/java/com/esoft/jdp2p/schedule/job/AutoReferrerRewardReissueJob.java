package com.esoft.jdp2p.schedule.job;

import com.esoft.archer.user.service.impl.ReferrerRewardReissueService;
import com.esoft.core.annotations.Logger;
import org.apache.commons.logging.Log;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2015/9/2.
 */
@Component
public class AutoReferrerRewardReissueJob implements Job {

    @Logger
    Log log;

    @Autowired
    private ReferrerRewardReissueService referrerRewardReissueService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            referrerRewardReissueService.reward();
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
            throw e;
        }
    }

}
