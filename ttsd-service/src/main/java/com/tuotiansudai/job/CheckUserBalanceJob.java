package com.tuotiansudai.job;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

@Deprecated
@Component
public class CheckUserBalanceJob implements Job {

    static Logger logger = Logger.getLogger(CheckUserBalanceJob.class);

//    @Autowired
//    private CheckUserBalanceService checkUserBalanceService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("trigger CheckUserBalanceJob job");
//        checkUserBalanceService.checkUserBalance();
        logger.info("complete CheckUserBalanceJob job");
    }
}
