package com.tuotiansudai.scheduler.plugin;

import com.tuotiansudai.job.*;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.spi.ClassLoadHelper;
import org.quartz.spi.SchedulerPlugin;

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
            deleteCalculateDefaultInterest();
        }
        if (JobType.AutoReFreshAreaByMobile.name().equalsIgnoreCase(schedulerName)) {
            deleteRefreshAreaByMobile();
        }
        if (JobType.LoanRepayNotify.name().equalsIgnoreCase(schedulerName)) {
            deleteLoanRepayNotifyJob();
        }
        if (JobType.BirthdayNotify.name().equalsIgnoreCase(schedulerName)) {
            deleteBirthdayNotifyJob();
        }
        if (JobType.ExperienceRepay.name().equals(schedulerName)) {
            deleteNewbieExperienceRepayJob();
        }
        if (JobType.CheckUserBalanceMonthly.name().equals(schedulerName)) {
            deleteCheckUserBalanceJob();
        }
        if (JobType.CouponRepayCallBack.name().equalsIgnoreCase(schedulerName)) {
            deleteCouponRepayCallBackJobIfNotExist();
        }
        if (JobType.ExtraRateRepayCallBack.name().equalsIgnoreCase(schedulerName)) {
            createExtraRateRepayCallBackIfNotExist();
        }
        if (JobType.PlatformBalanceLowNotify.name().equals(schedulerName)) {
            deletePlatformBalanceLowNotifyJob();
        }
        if (JobType.EventMessage.name().equals(schedulerName)) {
            deleteEventMessageJob();
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

    private void deleteCouponRepayCallBackJobIfNotExist(){
        jobManager.deleteJob(JobType.CouponRepayCallBack, JobType.CouponRepayCallBack.name(), JobType.CouponRepayCallBack.name());
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

    private void deleteRefreshAreaByMobile() {
        jobManager.deleteJob(JobType.AutoReFreshAreaByMobile, JobType.AutoReFreshAreaByMobile.name(), JobType.AutoReFreshAreaByMobile.name());
    }

    private void deleteBirthdayNotifyJob() {
        jobManager.deleteJob(JobType.BirthdayNotify, JobType.BirthdayNotify.name(), JobType.BirthdayNotify.name());
    }

    private void deleteCalculateDefaultInterest() {
        jobManager.deleteJob(JobType.CalculateDefaultInterest, JobType.CalculateDefaultInterest.name(), JobType.CalculateDefaultInterest.name());
    }

    private void deleteNewbieExperienceRepayJob() {
        jobManager.deleteJob(JobType.ExperienceRepay, JobType.ExperienceRepay.name(), JobType.ExperienceRepay.name());
    }

    private void deleteCheckUserBalanceJob() {
        jobManager.deleteJob(JobType.CheckUserBalanceMonthly, JobType.CheckUserBalanceMonthly.name(), JobType.CheckUserBalanceMonthly.name());
    }

    private void deleteLoanRepayNotifyJob() {
        jobManager.deleteJob(JobType.LoanRepayNotify, JobType.LoanRepayNotify.name(), JobType.LoanRepayNotify.name());
    }

    private void deletePlatformBalanceLowNotifyJob() {
        jobManager.deleteJob(JobType.PlatformBalanceLowNotify, JobType.PlatformBalanceLowNotify.name(), JobType.PlatformBalanceLowNotify.name());
    }

    private void deleteEventMessageJob() {
        jobManager.deleteJob(JobType.EventMessage, JobType.EventMessage.name(), JobType.EventMessage.name());
    }
}
