package com.tuotiansudai.utils;

import com.tuotiansudai.utils.quartz.TriggeredJobBuilder;
import org.apache.log4j.Logger;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JobManager {
    static Logger log = Logger.getLogger(JobManager.class);

    @Autowired
    private Scheduler scheduler;

    public void submitJob(JobDetail job, Trigger trigger) {
        try {
            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            log.error(e.getLocalizedMessage(), e);
        }
    }

    public TriggeredJobBuilder newJob(Class<? extends Job> jobClazz){
        return new TriggeredJobBuilder(jobClazz, this);
    }
}
