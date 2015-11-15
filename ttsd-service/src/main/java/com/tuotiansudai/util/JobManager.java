package com.tuotiansudai.util;

import com.tuotiansudai.job.JobType;
import com.tuotiansudai.util.quartz.SchedulerBuilder;
import com.tuotiansudai.util.quartz.ThreadPoolBuilder;
import com.tuotiansudai.util.quartz.TriggeredJobBuilder;
import org.quartz.*;
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

    public void deleteJob(String jobGroup, String jobName) {
        deleteJob(JobType.Default, jobGroup, jobName);
    }

    public JobDetail findJobDetail(String jobGroup, String jobName) {
        return findJobDetail(JobType.Default, jobGroup, jobName);
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

    public void deleteJob(JobType jobType, String jobGroup, String jobName) {
        String schedulerName = "Scheduler-" + jobType.name();
        try {
            Scheduler scheduler = schedulerBuilder.buildScheduler(schedulerName, threadPool);
            scheduler.deleteJob(JobKey.jobKey(jobName, jobGroup));
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public JobDetail findJobDetail(JobType jobType, String jobGroup, String jobName) {
        String schedulerName = "Scheduler-" + jobType.name();
        try {
            Scheduler scheduler = schedulerBuilder.buildScheduler(schedulerName, threadPool);
            return scheduler.getJobDetail(JobKey.jobKey(jobName, jobGroup));
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        threadPool = ThreadPoolBuilder.buildMiniThreadPool();
    }
}
