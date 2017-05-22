package com.tuotiansudai.scheduler.plugin;

import com.tuotiansudai.job.JobManager;
import com.tuotiansudai.job.JobType;
import org.apache.log4j.Logger;
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
        this.removeUnusedJobs();
    }

    @Override
    public void shutdown() {

    }

    private void removeUnusedJobs() {
        jobManager.deleteJob(JobType.NormalRepayCallBack, "umpay", "normal_repay_call_back");
        jobManager.deleteJob(JobType.AdvanceRepayCallBack, "umpay", "advance_repay_call_back");
        jobManager.deleteJob(JobType.OverInvestPayBack, "umpay", "invest_call_back");
        jobManager.deleteJob(JobType.AutoJPushNoInvestAlert, "AutoJPushNoInvestAlert", "AutoJPushNoInvestAlert");
        jobManager.deleteJob(JobType.AutoJPushAlertBirthDay, "AutoJPushAlertBirthDay", "AutoJPushAlertBirthDay");
    }
}
