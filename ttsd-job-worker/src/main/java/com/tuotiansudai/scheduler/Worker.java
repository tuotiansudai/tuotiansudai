package com.tuotiansudai.scheduler;

import com.tuotiansudai.job.InvestCallback;
import com.tuotiansudai.job.JobType;
import com.tuotiansudai.utils.JobManager;
import com.tuotiansudai.utils.quartz.AutowiringSpringBeanJobFactory;
import com.tuotiansudai.utils.quartz.JobStoreBuilder;
import com.tuotiansudai.utils.quartz.SchedulerBuilder;
import com.tuotiansudai.utils.quartz.ThreadPoolBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.spi.JobStore;
import org.quartz.spi.ThreadPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Worker {

    @Autowired
    private AutowiringSpringBeanJobFactory jobFactory;

    @Autowired
    private SchedulerBuilder schedulerBuilder;

    @Autowired
    private JobStoreBuilder jobStoreBuilder;

    @Autowired
    private JobManager jobManager;

    public void start() {
        String[] schedulerNames = JobConfig.schedulerNames.split(",");
        ThreadPool threadPool = ThreadPoolBuilder.buildThreadPool(JobConfig.threadCount, JobConfig.threadPriority);
        for (String schedulerName : schedulerNames) {
            if (JobType.OverInvestPayBack.name().equalsIgnoreCase(schedulerName)) {
                createInvestCallBackJobIfNotExist();
            }
            String fullSchedulerName = "Scheduler-" + schedulerName.trim();
            JobStore jobStore = jobStoreBuilder.buildJdbcJobStore(
                    fullSchedulerName,
                    JobConfig.misfireThreshold, JobConfig.maxMisfiresToHandleAtATime,
                    JobConfig.isClustered, JobConfig.clusterCheckinInterval);
            startScheduler(fullSchedulerName, threadPool, jobStore);
        }
    }

    private void startScheduler(String schedulerName, ThreadPool threadPool, JobStore jobStore) {
        try {
            Scheduler scheduler = schedulerBuilder.buildScheduler(schedulerName, threadPool, jobStore);
            scheduler.setJobFactory(jobFactory);
            scheduler.start();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    private void createInvestCallBackJobIfNotExist() {
        final JobType jobType = JobType.OverInvestPayBack;
        final String jobGroup = InvestCallback.JOB_GROUP;
        final String jobName = InvestCallback.JOB_NAME;
        JobDetail jobDetail = jobManager.findJobDetail(jobType, jobGroup, jobName);
        if (jobDetail != null) {
            jobManager.deleteJob(jobType, jobGroup, jobName);
        }
        try {
            jobManager.newJob(jobType, InvestCallback.class)
                    .runWithSchedule(SimpleScheduleBuilder
                            .repeatSecondlyForever(InvestCallback.RUN_INTERVAL_SECONDS)
                            .withMisfireHandlingInstructionIgnoreMisfires())
                    .withIdentity(jobGroup, jobName)
                    .submit();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
