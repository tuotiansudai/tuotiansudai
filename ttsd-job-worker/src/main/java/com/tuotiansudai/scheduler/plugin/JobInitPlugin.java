package com.tuotiansudai.scheduler.plugin;

import com.tuotiansudai.job.AssignFirstRedEnvelopSplitJob;
import com.tuotiansudai.job.AssignSecondRedEnvelopSplitJob;
import com.tuotiansudai.job.JobManager;
import com.tuotiansudai.job.JobType;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
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
        if (JobType.CouponRepayCallBack.name().equalsIgnoreCase(schedulerName)) {
            deleteCouponRepayCallBackJobIfNotExist();
        }
        if (JobType.ExtraRateRepayCallBack.name().equalsIgnoreCase(schedulerName)) {
            deleteExtraRateRepayCallBackIfNotExist();
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

    private void deleteCouponRepayCallBackJobIfNotExist() {
        jobManager.deleteJob(JobType.CouponRepayCallBack, "umpay", "coupon_repay_call_back");
    }

    private void deleteExtraRateRepayCallBackIfNotExist() {
        jobManager.deleteJob(JobType.ExtraRateRepayCallBack, "umpay", "repay_extra_rate_invest_call_back");
    }

}
