package com.tuotiansudai.scheduler;

import com.tuotiansudai.utils.quartz.AutowiringSpringBeanJobFactory;
import com.tuotiansudai.utils.quartz.JobStoreBuilder;
import com.tuotiansudai.utils.quartz.SchedulerBuilder;
import com.tuotiansudai.utils.quartz.ThreadPoolBuilder;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
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

    public void start() {
        String[] schedulerNames = JobConfig.schedulerNames.split(",");
        ThreadPool threadPool = ThreadPoolBuilder.buildThreadPool(JobConfig.threadCount, JobConfig.threadPriority);
        for (String schedulerName : schedulerNames) {
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
}
