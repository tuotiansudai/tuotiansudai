package com.tuotiansudai.job;

import com.tuotiansudai.service.PullTopicService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;


public class RedEnvelopeJob implements Job {

    public static final int RUN_INTERVAL_SECONDS = 2;
    @Autowired
    private PullTopicService pullTopicService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        pullTopicService.processSynchRedEnvelope();
    }
}


