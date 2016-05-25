package com.tuotiansudai.scheduler.plugin;

import com.tuotiansudai.job.*;
import com.tuotiansudai.point.job.ImitateLotteryJob;
import com.tuotiansudai.util.JobManager;
import org.apache.log4j.Logger;
import org.quartz.*;
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
        if (JobType.InvestCallBack.name().equalsIgnoreCase(schedulerName)) {
            createInvestCallBackJobIfNotExist();
        }
        if (JobType.InvestTransferCallBack.name().equalsIgnoreCase(schedulerName)) {
            createInvestTransferCallBackJobIfNotExist();
        }
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
        if (JobType.AutoJPushAlertBirthMonth.name().equalsIgnoreCase(schedulerName)) {
            createAutoJPushAlertBirthMonth();
        }
        if (JobType.AutoJPushAlertBirthDay.name().equalsIgnoreCase(schedulerName)) {
            createAutoJPushAlertBirthDay();
        }
        if (JobType.AutoJPushNoInvestAlert.name().equalsIgnoreCase(schedulerName)) {
            createAutoJPushNoInvestAlert();
        }
        if (JobType.ImitateLottery.name().equals(schedulerName)) {
            createImitateLotteryJob();
        }
        if (JobType.CheckUserBalanceMonthly.name().equals(schedulerName)) {
            createCheckUserBalanceJob();
        }
    }

    @Override
    public void shutdown() {

    }

    private void createInvestCallBackJobIfNotExist() {
        final JobType jobType = JobType.InvestCallBack;
        final String jobGroup = InvestCallbackJob.JOB_GROUP;
        final String jobName = InvestCallbackJob.JOB_NAME;
        try {
            jobManager.newJob(jobType, InvestCallbackJob.class)
                    .replaceExistingJob(true)
                    .runWithSchedule(SimpleScheduleBuilder
                            .repeatSecondlyForever(InvestCallbackJob.RUN_INTERVAL_SECONDS)
                            .withMisfireHandlingInstructionIgnoreMisfires())
                    .withIdentity(jobGroup, jobName)
                    .submit();
        } catch (SchedulerException e) {
            logger.debug(e.getLocalizedMessage(), e);
        }
    }

    private void createInvestTransferCallBackJobIfNotExist() {
        final JobType jobType = JobType.InvestTransferCallBack;
        final String jobGroup = InvestTransferCallbackJob.JOB_GROUP;
        final String jobName = InvestTransferCallbackJob.JOB_NAME;
        try {
            jobManager.newJob(jobType, InvestTransferCallbackJob.class)
                    .replaceExistingJob(true)
                    .runWithSchedule(SimpleScheduleBuilder
                            .repeatSecondlyForever(InvestTransferCallbackJob.RUN_INTERVAL_SECONDS)
                            .withMisfireHandlingInstructionIgnoreMisfires())
                    .withIdentity(jobGroup, jobName)
                    .submit();
        } catch (SchedulerException e) {
            logger.debug(e.getLocalizedMessage(), e);
        }
    }

    private void createImitateLotteryJob() {
        try {
            jobManager.newJob(JobType.ImitateLottery, ImitateLotteryJob.class).replaceExistingJob(true)
                    .runWithSchedule(CronScheduleBuilder.cronSchedule("0 0/5 * * * ?").inTimeZone(TimeZone.getTimeZone(TIMEZONE_SHANGHAI)))
                    .withIdentity(JobType.ImitateLottery.name(), JobType.ImitateLottery.name()).submit();
        } catch (SchedulerException e) {
            logger.debug(e.getLocalizedMessage(), e);
        }
    }

    private void createCalculateDefaultInterest() {
        try {
            jobManager.newJob(JobType.CalculateDefaultInterest, CalculateDefaultInterestJob.class).replaceExistingJob(true)
                    .runWithSchedule(CronScheduleBuilder.cronSchedule("0 0 1 * * ? *").inTimeZone(TimeZone.getTimeZone(TIMEZONE_SHANGHAI)))
                    .withIdentity(JobType.CalculateDefaultInterest.name(), JobType.CalculateDefaultInterest.name()).submit();
        } catch (SchedulerException e) {
            logger.debug(e.getLocalizedMessage(), e);
        }
    }


    private void createRefreshAreaByMobile() {
        try {
            jobManager.newJob(JobType.AutoReFreshAreaByMobile, AutoReFreshAreaByMobileJob.class).replaceExistingJob(true)
                    .runWithSchedule(CronScheduleBuilder.cronSchedule("0 0 2 * * ? *").inTimeZone(TimeZone.getTimeZone(TIMEZONE_SHANGHAI)))
                    .withIdentity(JobType.AutoReFreshAreaByMobile.name(), JobType.AutoReFreshAreaByMobile.name()).submit();
        } catch (SchedulerException e) {
            logger.debug(e.getLocalizedMessage(), e);
        }
    }

    private void createAutoJPushAlertBirthMonth() {
        try {
            jobManager.newJob(JobType.AutoJPushAlertBirthMonth, AutoJPushAlertBirthMonthJob.class).replaceExistingJob(true)
                    .runWithSchedule(CronScheduleBuilder.cronSchedule("0 0 12 1 * ? *").inTimeZone(TimeZone.getTimeZone(TIMEZONE_SHANGHAI)))
                    .withIdentity(JobType.AutoJPushAlertBirthMonth.name(), JobType.AutoJPushAlertBirthMonth.name()).submit();

        } catch (SchedulerException e) {
            logger.debug(e.getLocalizedMessage(), e);
        }
    }

    private void createAutoJPushAlertBirthDay() {
        try {
            jobManager.newJob(JobType.AutoJPushAlertBirthDay, AutoJPushAlertBirthDayJob.class).replaceExistingJob(true)
                    .runWithSchedule(CronScheduleBuilder.cronSchedule("0 0 9 * * ? *").inTimeZone(TimeZone.getTimeZone(TIMEZONE_SHANGHAI)))
                    .withIdentity(JobType.AutoJPushAlertBirthDay.name(), JobType.AutoJPushAlertBirthDay.name()).submit();

        } catch (SchedulerException e) {
            logger.debug(e.getLocalizedMessage(), e);
        }
    }

    private void createAutoJPushNoInvestAlert() {
        try {
            jobManager.newJob(JobType.AutoJPushNoInvestAlert, AutoJPushNoInvestAlertJob.class).replaceExistingJob(true)
                    .runWithSchedule(CronScheduleBuilder.cronSchedule("0 30 9 * * ? *").inTimeZone(TimeZone.getTimeZone(TIMEZONE_SHANGHAI)))
                    .withIdentity(JobType.AutoJPushNoInvestAlert.name(), JobType.AutoJPushNoInvestAlert.name()).submit();

        } catch (SchedulerException e) {
            logger.debug(e.getLocalizedMessage(), e);
        }
    }

    private void createLoanRepayNotifyJob() {
        try {
            jobManager.newJob(JobType.LoanRepayNotify, LoanRepayNotifyJob.class).replaceExistingJob(true)
                    .runWithSchedule(CronScheduleBuilder.cronSchedule("0 0 14 * * ? *").inTimeZone(TimeZone.getTimeZone(TIMEZONE_SHANGHAI)))
                    .withIdentity(JobType.LoanRepayNotify.name(), JobType.LoanRepayNotify.name()).submit();
        } catch (SchedulerException e) {
            logger.debug(e.getLocalizedMessage(), e);
        }
    }

    private void createBirthdayNotifyJob() {
        try {
            jobManager.newJob(JobType.BirthdayNotify, BirthdayNotifyJob.class).replaceExistingJob(true)
                    .runWithSchedule(CronScheduleBuilder.cronSchedule("0 0 12 5 * ? *").inTimeZone(TimeZone.getTimeZone(TIMEZONE_SHANGHAI)))
                    .withIdentity(JobType.BirthdayNotify.name(), JobType.BirthdayNotify.name()).submit();
        } catch (SchedulerException e) {
            logger.debug(e.getLocalizedMessage(), e);
        }
    }

    private void createCheckUserBalanceJob() {
        try {
            jobManager.newJob(JobType.CheckUserBalanceMonthly, CheckUserBalanceJob.class).replaceExistingJob(true)
                    .runWithSchedule(CronScheduleBuilder.cronSchedule("0 0 1 ? * 7#1 *").inTimeZone(TimeZone.getTimeZone(TIMEZONE_SHANGHAI)))
                    .withIdentity(JobType.CheckUserBalanceMonthly.name(), JobType.CheckUserBalanceMonthly.name()).submit();
        } catch (SchedulerException e) {
            logger.debug(e.getLocalizedMessage(), e);
        }
    }
}
