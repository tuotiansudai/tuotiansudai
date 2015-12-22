package com.tuotiansudai.scheduler.plugin;

import com.tuotiansudai.job.*;
import com.tuotiansudai.util.JobManager;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.quartz.CronScheduleBuilder;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.spi.ClassLoadHelper;
import org.quartz.spi.SchedulerPlugin;

import java.util.TimeZone;

public class JobInitPlugin implements SchedulerPlugin {

    private static Logger logger = Logger.getLogger(JobInitPlugin.class);

    private JobManager jobManager;

    private String schedulerName;

    public JobInitPlugin(JobManager jobManager) {
        this.jobManager = jobManager;
    }

    @Override
    public void initialize(String name, Scheduler scheduler, ClassLoadHelper loadHelper) throws SchedulerException {
        this.schedulerName = scheduler.getSchedulerName().replaceFirst("^Scheduler-", "");
    }


    @Override
    public void start() {
        if (JobType.OverInvestPayBack.name().equalsIgnoreCase(schedulerName)) {
            createInvestCallBackJobIfNotExist();
        }
        if (JobType.CalculateDefaultInterest.name().equalsIgnoreCase(schedulerName)) {
            createCalculateDefaultInterest();
        }
        if (JobType.AutoReFreshAreaByMobile.name().equalsIgnoreCase(schedulerName)) {
            createRefreshAreaByMobile();
        }
    }

    @Override
    public void shutdown() {

    }

    private void createInvestCallBackJobIfNotExist() {
        final JobType jobType = JobType.OverInvestPayBack;
        final String jobGroup = InvestCallback.JOB_GROUP;
        final String jobName = InvestCallback.JOB_NAME;
        try {
            jobManager.newJob(jobType, InvestCallback.class)
                    .replaceExistingJob(true)
                    .runWithSchedule(SimpleScheduleBuilder
                            .repeatSecondlyForever(InvestCallback.RUN_INTERVAL_SECONDS)
                            .withMisfireHandlingInstructionIgnoreMisfires())
                    .withIdentity(jobGroup, jobName)
                    .submit();
        } catch (SchedulerException e) {
            logger.debug(e.getLocalizedMessage(), e);
        }
    }

    private void createCalculateDefaultInterest() {
        try {
            jobManager.newJob(JobType.CalculateDefaultInterest, CalculateDefaultInterestJob.class).replaceExistingJob(true)
                    .runWithSchedule(CronScheduleBuilder.cronSchedule("0 0 1 * * ? *").inTimeZone(TimeZone.getTimeZone("Asia/Shanghai")))
                    .withIdentity(JobType.CalculateDefaultInterest.name(), JobType.CalculateDefaultInterest.name()).submit();
        } catch (SchedulerException e) {
            logger.debug(e.getLocalizedMessage(), e);
        }
    }

    private void createRefreshAreaByMobile() {
        try {
            jobManager.newJob(JobType.AutoReFreshAreaByMobile, AutoReFreshAreaByMobileJob.class).replaceExistingJob(true)
                    .runWithSchedule(CronScheduleBuilder.cronSchedule("0 0 2 * * ? *").inTimeZone(TimeZone.getTimeZone("Asia/Shanghai")))
                    .withIdentity(JobType.AutoReFreshAreaByMobile.name(), JobType.AutoReFreshAreaByMobile.name()).submit();
        } catch (SchedulerException e) {
            logger.debug(e.getLocalizedMessage(), e);
        }
    }
}
