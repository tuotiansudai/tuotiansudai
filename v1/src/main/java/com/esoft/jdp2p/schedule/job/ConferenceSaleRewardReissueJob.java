package com.esoft.jdp2p.schedule.job;

import com.ttsd.special.services.ConferenceSaleService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ConferenceSaleRewardReissueJob implements Job {

    @Autowired
    private ConferenceSaleService conferenceSaleService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Date deadlineDate = new Date();
        conferenceSaleService.reissueMissedReward(deadlineDate);
    }
}
