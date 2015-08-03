package com.esoft.jdp2p.schedule.job;

import com.esoft.archer.user.service.impl.RepayingLoanReferrerRewardService;
import com.esoft.core.annotations.Logger;
import org.apache.commons.logging.Log;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2015/8/2.
 */
@Component
public class RepayingLoanReferrerRewardJob implements Job {

    @Autowired
    private RepayingLoanReferrerRewardService repayingLoanReferrerRewardService;

    @Logger
    Log log;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            repayingLoanReferrerRewardService.reward();
        } catch (Exception e) {
            //log.error(e);
            //throw e;
            e.printStackTrace();
        }
    }
}
