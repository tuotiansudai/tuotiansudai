package com.tuotiansudai.point.job;


import com.tuotiansudai.point.service.PointLotteryService;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ImitateLotteryJob implements Job{

    static Logger logger = Logger.getLogger(ImitateLotteryJob.class);

    @Autowired
    private PointLotteryService pointLotteryService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("trigger ImitateLotteryJob job");
        pointLotteryService.imitateLottery();
    }

}
