package com.tuotiansudai.utils;

import com.tuotiansudai.job.JobType;
import com.tuotiansudai.utils.quartz.SchedulerBuilder;
import com.tuotiansudai.utils.quartz.ThreadPoolBuilder;
import com.tuotiansudai.utils.quartz.TriggeredJobBuilder;
import org.quartz.Job;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.spi.ThreadPool;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * example:
 * <pre>
 *     // set target job class
 *     jobmanager.newjob(testjob.class)
 *
 *     // [optional] add parameters
 *     .addjobdata(some_parameters)
 *
 *     // [optional] add description
 *     .withdescription(some_parameters)
 *
 *     // [optional] add identity
 *     .withidentity(some_parameters)
 *
 *     // run once
 *     .runonceat(some_date)
 *
 *     // or run with schedule
 *     .runwithschedule(simpleschedulebuilder....)
 *
 *     // submit job to schedule
 *     .submit();
 * </pre>
 */
@Component
public class JobManager implements InitializingBean {

    private ThreadPool threadPool;

    @Autowired
    private SchedulerBuilder schedulerBuilder;

    public TriggeredJobBuilder newJob(Class<? extends Job> jobClazz) {
        return newJob(JobType.Default, jobClazz);
    }

    public TriggeredJobBuilder newJob(JobType jobType, Class<? extends Job> jobClazz) {
        String schedulerName = "Scheduler-" + jobType.name();
        Scheduler scheduler = null;
        try {
            scheduler = schedulerBuilder.buildScheduler(schedulerName, threadPool);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return TriggeredJobBuilder.newJob(jobClazz, scheduler);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        threadPool = ThreadPoolBuilder.buildMiniThreadPool();
    }
}
