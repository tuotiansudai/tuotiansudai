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
            deleteExtraRateRepayCallBackIfNotExist();
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

    private void deleteCouponRepayCallBackJobIfNotExist(){
        jobManager.deleteJob(JobType.CouponRepayCallBack,"umpay","coupon_repay_call_back");
    }

    private void deleteExtraRateRepayCallBackIfNotExist() {
        jobManager.deleteJob(JobType.ExtraRateRepayCallBack, "umpay", "repay_extra_rate_invest_call_back");
    }
   
}
