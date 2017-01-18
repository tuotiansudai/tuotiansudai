package com.tuotiansudai.job;

import com.tuotiansudai.quartz.SchedulerBuilder;
import com.tuotiansudai.quartz.ThreadPoolBuilder;
import com.tuotiansudai.quartz.TriggeredJobBuilder;
import org.apache.log4j.Logger;
import org.quartz.*;
import org.quartz.spi.ThreadPool;
import org.springframework.beans.factory.InitializingBean;

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
public class JobManager implements InitializingBean {
    static Logger logger = Logger.getLogger(JobManager.class);

    private ThreadPool threadPool;

    private final SchedulerBuilder schedulerBuilder;

    public JobManager(SchedulerBuilder schedulerBuilder) {
        this.schedulerBuilder = schedulerBuilder;
    }

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
            logger.error("create job failed, build scheduler error", e);
        }
        return TriggeredJobBuilder.newJob(jobClazz, scheduler);
    }

    public void deleteJob(JobType jobType, String jobGroup, String jobName) {
        String schedulerName = "Scheduler-" + jobType.name();
        try {
            Scheduler scheduler = schedulerBuilder.buildScheduler(schedulerName, threadPool);
            scheduler.deleteJob(JobKey.jobKey(jobName, jobGroup));
        } catch (SchedulerException e) {
            logger.error("build scheduler error", e);
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
