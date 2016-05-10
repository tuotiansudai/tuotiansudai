package com.tuotiansudai.job;

import com.tuotiansudai.service.OperationDataService;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Created by huoxuanbo on 16/5/9.
 */
public class RefreshInformationInRedisJob implements Job {
    static Logger logger = Logger.getLogger(BirthdayNotifyJob.class);

    @Autowired
    private OperationDataService operationDataService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("trigger RefreshInformationInRedisJob job");
        operationDataService.updateRedis();
    }
}
