package com.tuotiansudai.scheduler.plugin;

import com.tuotiansudai.job.*;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
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

    private final String TIMEZONE_SHANGHAI = "Asia/Shanghai";

    public JobInitPlugin(JobManager jobManager) {
        this.jobManager = jobManager;
    }

    @Override
    public void initialize(String name, Scheduler scheduler, ClassLoadHelper loadHelper) throws SchedulerException {
        this.schedulerName = scheduler.getSchedulerName().replaceFirst("^Scheduler-", "");
    }

    @Override
    public void start() {
        if (JobType.CalculateDefaultInterest.name().equalsIgnoreCase(schedulerName)) {
            createCalculateDefaultInterest();
        }
        if (JobType.AutoReFreshAreaByMobile.name().equalsIgnoreCase(schedulerName)) {
            createRefreshAreaByMobile();
        }
        if (JobType.LoanRepayNotify.name().equalsIgnoreCase(schedulerName)) {
            createLoanRepayNotifyJob();
        }
        if (JobType.BirthdayNotify.name().equalsIgnoreCase(schedulerName)) {
            createBirthdayNotifyJob();
        }
        if (JobType.ExperienceRepay.name().equals(schedulerName)) {
            removeNewbieExperienceRepayJob();
        }
        if (JobType.CheckUserBalanceMonthly.name().equals(schedulerName)) {
            createCheckUserBalanceJob();
        }
        if (JobType.CouponRepayCallBack.name().equalsIgnoreCase(schedulerName)) {
            createCouponRepayCallBackJobIfNotExist();
        }
        if (JobType.ExtraRateRepayCallBack.name().equalsIgnoreCase(schedulerName)) {
            createExtraRateRepayCallBackIfNotExist();
        }
        if (JobType.PlatformBalanceLowNotify.name().equals(schedulerName)) {
            platformBalanceLowNotifyJob();
        }
        if (JobType.EventMessage.name().equals(schedulerName)) {
            eventMessageJob();
        }
        if (JobType.SendFirstRedEnvelopSplit.name().equalsIgnoreCase(schedulerName)) {
            createFirstRedEnvelopSplitJob();
        }
        if (JobType.SendSecondRedEnvelopSplit.name().equalsIgnoreCase(schedulerName)) {
            createSecondRedEnvelopSplitJob();
        }
    }

    @Override
    public void shutdown() {

    }

    private void createCalculateDefaultInterest() {
        try {
            jobManager.newJob(JobType.CalculateDefaultInterest, CalculateDefaultInterestJob.class).replaceExistingJob(true)
                    .runWithSchedule(CronScheduleBuilder.cronSchedule("0 0 1 * * ? *").inTimeZone(TimeZone.getTimeZone(TIMEZONE_SHANGHAI)))
                    .withIdentity(JobType.CalculateDefaultInterest.name(), JobType.CalculateDefaultInterest.name()).submit();
        } catch (SchedulerException e) {
            logger.info(e.getLocalizedMessage(), e);
        }
    }

    private void createRefreshAreaByMobile() {
        try {
            jobManager.newJob(JobType.AutoReFreshAreaByMobile, AutoReFreshAreaByMobileJob.class).replaceExistingJob(true)
                    .runWithSchedule(CronScheduleBuilder.cronSchedule("0 0 2 * * ? *").inTimeZone(TimeZone.getTimeZone(TIMEZONE_SHANGHAI)))
                    .withIdentity(JobType.AutoReFreshAreaByMobile.name(), JobType.AutoReFreshAreaByMobile.name()).submit();
        } catch (SchedulerException e) {
            logger.info(e.getLocalizedMessage(), e);
        }
    }

    private void createLoanRepayNotifyJob() {
        try {
            jobManager.newJob(JobType.LoanRepayNotify, LoanRepayNotifyJob.class).replaceExistingJob(true)
                    .runWithSchedule(CronScheduleBuilder.cronSchedule("0 0 14 * * ? *").inTimeZone(TimeZone.getTimeZone(TIMEZONE_SHANGHAI)))
                    .withIdentity(JobType.LoanRepayNotify.name(), JobType.LoanRepayNotify.name()).submit();
        } catch (SchedulerException e) {
            logger.info(e.getLocalizedMessage(), e);
        }
    }

    private void createBirthdayNotifyJob() {
        try {
            jobManager.newJob(JobType.BirthdayNotify, BirthdayNotifyJob.class).replaceExistingJob(true)
                    .runWithSchedule(CronScheduleBuilder.cronSchedule("0 0 12 5 * ? *").inTimeZone(TimeZone.getTimeZone(TIMEZONE_SHANGHAI)))
                    .withIdentity(JobType.BirthdayNotify.name(), JobType.BirthdayNotify.name()).submit();
        } catch (SchedulerException e) {
            logger.info(e.getLocalizedMessage(), e);
        }
    }

    private void removeNewbieExperienceRepayJob() {
        jobManager.deleteJob(JobType.ExperienceRepay, JobType.ExperienceRepay.name(), JobType.ExperienceRepay.name());
    }

    private void createCheckUserBalanceJob() {
        try {
            jobManager.newJob(JobType.CheckUserBalanceMonthly, CheckUserBalanceJob.class).replaceExistingJob(true)
                    .runWithSchedule(CronScheduleBuilder.cronSchedule("0 30 1 ? * 7#1 *").inTimeZone(TimeZone.getTimeZone(TIMEZONE_SHANGHAI)))
                    .withIdentity(JobType.CheckUserBalanceMonthly.name(), JobType.CheckUserBalanceMonthly.name()).submit();
        } catch (SchedulerException e) {
            logger.info(e.getLocalizedMessage(), e);
        }
    }

    private void createCouponRepayCallBackJobIfNotExist() {
        final JobType jobType = JobType.CouponRepayCallBack;
        final String jobGroup = CouponRepayNotifyCallbackJob.JOB_GROUP;
        final String jobName = CouponRepayNotifyCallbackJob.JOB_NAME;
        try {
            jobManager.newJob(jobType, CouponRepayNotifyCallbackJob.class)
                    .replaceExistingJob(true)
                    .runWithSchedule(SimpleScheduleBuilder
                            .repeatSecondlyForever(CouponRepayNotifyCallbackJob.RUN_INTERVAL_SECONDS)
                            .withMisfireHandlingInstructionIgnoreMisfires())
                    .withIdentity(jobGroup, jobName)
                    .submit();
        } catch (SchedulerException e) {
            logger.info(e.getLocalizedMessage(), e);
        }
    }

    private void createExtraRateRepayCallBackIfNotExist() {
        final JobType jobType = JobType.ExtraRateRepayCallBack;
        final String jobGroup = ExtraRateInvestCallbackJob.JOB_GROUP;
        final String jobName = ExtraRateInvestCallbackJob.JOB_NAME;
        try {
            jobManager.newJob(jobType, ExtraRateInvestCallbackJob.class)
                    .replaceExistingJob(true)
                    .runWithSchedule(SimpleScheduleBuilder
                            .repeatSecondlyForever(ExtraRateInvestCallbackJob.RUN_INTERVAL_SECONDS)
                            .withMisfireHandlingInstructionIgnoreMisfires())
                    .withIdentity(jobGroup, jobName)
                    .submit();
        } catch (SchedulerException e) {
            logger.info(e.getLocalizedMessage(), e);
        }
    }

    private void platformBalanceLowNotifyJob() {
        try {
            jobManager.newJob(JobType.PlatformBalanceLowNotify, PlatformBalanceMonitorJob.class).replaceExistingJob(true)
                    .runWithSchedule(CronScheduleBuilder.cronSchedule("0 0 9 * * ? *").inTimeZone(TimeZone.getTimeZone(TIMEZONE_SHANGHAI)))
                    .withIdentity(JobType.PlatformBalanceLowNotify.name(), JobType.PlatformBalanceLowNotify.name()).submit();
        } catch (SchedulerException e) {
            logger.info(e.getLocalizedMessage(), e);
        }
    }

    private void eventMessageJob() {
        try {
            jobManager.newJob(JobType.EventMessage, EventMessageJob.class).replaceExistingJob(true)
                    .runWithSchedule(CronScheduleBuilder.cronSchedule("0 0 10 * * ? *").inTimeZone(TimeZone.getTimeZone(TIMEZONE_SHANGHAI)))
                    .withIdentity(JobType.EventMessage.name(), JobType.EventMessage.name()).submit();
        } catch (SchedulerException e) {
            logger.info(e.getLocalizedMessage(), e);
        }
    }

    private void createFirstRedEnvelopSplitJob() {
        try {
            jobManager.newJob(JobType.SendFirstRedEnvelopSplit, AssignFirstRedEnvelopSplitJob.class)
                    .withIdentity(JobType.SendFirstRedEnvelopSplit.name(), JobType.SendFirstRedEnvelopSplit.name())
                    .replaceExistingJob(true)
                    .runOnceAt(DateTime.parse(AssignFirstRedEnvelopSplitJob.JOB_EXECUTE_TIME, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate()).submit();
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }

    private void createSecondRedEnvelopSplitJob() {
        try {
            jobManager.newJob(JobType.SendSecondRedEnvelopSplit, AssignSecondRedEnvelopSplitJob.class)
                    .withIdentity(JobType.SendSecondRedEnvelopSplit.name(), JobType.SendSecondRedEnvelopSplit.name())
                    .replaceExistingJob(true)
                    .runOnceAt(DateTime.parse(AssignSecondRedEnvelopSplitJob.JOB_EXECUTE_TIME, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate()).submit();
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }
}
