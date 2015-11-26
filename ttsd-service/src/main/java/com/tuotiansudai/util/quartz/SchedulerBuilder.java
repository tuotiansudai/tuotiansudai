package com.tuotiansudai.util.quartz;

import org.apache.log4j.Logger;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.spi.JobStore;
import org.quartz.spi.ThreadPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SchedulerBuilder {
    static Logger logger = Logger.getLogger(TriggeredJobBuilder.class);
    @Autowired
    private JobStoreBuilder jobStoreBuilder;

    public Scheduler buildScheduler(String schedulerName, ThreadPool threadPool) throws SchedulerException {
        JobStore jobStore = jobStoreBuilder.buildJdbcJobStore(schedulerName);
        return buildScheduler(schedulerName, threadPool, jobStore);
    }

    public Scheduler buildScheduler(String schedulerName, ThreadPool threadPool, JobStore jobStore) throws SchedulerException {
        return buildScheduler(schedulerName, threadPool, jobStore, true);
    }

    public Scheduler buildScheduler(String schedulerName, ThreadPool threadPool, JobStore jobStore, boolean jmxExport) throws SchedulerException {
        DirectSchedulerFactory schedulerFactory = DirectSchedulerFactory.getInstance();
        Scheduler scheduler = schedulerFactory.getScheduler(schedulerName);
        if (scheduler != null) {
            return scheduler;
        }
        schedulerFactory.createScheduler(
                schedulerName, "AUTO", threadPool, jobStore, null, null, 0, -1, -1, jmxExport, null);
        logger.info("create scheduler " + schedulerName + " success");
        return schedulerFactory.getScheduler(schedulerName);
    }
}
