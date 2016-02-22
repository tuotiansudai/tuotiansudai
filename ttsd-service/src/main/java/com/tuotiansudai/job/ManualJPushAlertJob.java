package com.tuotiansudai.job;

import com.tuotiansudai.jpush.service.JPushAlertService;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ManualJPushAlertJob implements Job{

    static Logger logger = Logger.getLogger(ManualJPushAlertJob.class);

    @Autowired
    private JPushAlertService jPushAlertService;

    public final static String JPUSH_ID = "JPUSH_ID";

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        long id = (long) context.getJobDetail().getJobDataMap().get(JPUSH_ID);
        logger.debug("ManualJPushAlertJob===========in, id = " + id);
        jPushAlertService.manualJPushAlert(id);
        logger.debug("ManualJPushAlertJob===========out, id = " + id );
    }

}
