package com.tuotiansudai.job;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@Deprecated
public class ExperienceRepayJob implements Job {
    static Logger logger = Logger.getLogger(LoanRepayNotifyJob.class);

//    @Autowired
//    private ExperienceRepayService experienceRepayService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("trigger NewbieExperienceRepayJob job");
//        experienceRepayService.repay(new Date());
    }
}
