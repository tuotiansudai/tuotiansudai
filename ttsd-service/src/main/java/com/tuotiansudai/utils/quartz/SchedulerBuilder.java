package com.tuotiansudai.utils.quartz;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.DirectSchedulerFactory;
import org.quartz.spi.JobStore;
import org.quartz.spi.ThreadPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SchedulerBuilder {
    @Autowired
    private JobStoreBuilder jobStoreBuilder;

    public Scheduler buildScheduler(String schedulerName, ThreadPool threadPool) throws SchedulerException {
        JobStore jobStore = jobStoreBuilder.buildJdbcJobStore(schedulerName);
        return buildScheduler(schedulerName, threadPool, jobStore);
    }

    public Scheduler buildScheduler(String schedulerName, ThreadPool threadPool, JobStore jobStore) throws SchedulerException {
        Scheduler scheduler = DirectSchedulerFactory.getInstance().getScheduler(schedulerName);
        if (scheduler != null) {
            return scheduler;
        }

        DirectSchedulerFactory.getInstance().createScheduler(
                schedulerName, "AUTO", threadPool, jobStore);
        return DirectSchedulerFactory.getInstance().getScheduler(schedulerName);
    }
}
