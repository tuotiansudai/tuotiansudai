package com.tuotiansudai.util.quartz;

import org.apache.log4j.Logger;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.spi.JobStore;
import org.quartz.spi.SchedulerPlugin;
import org.quartz.spi.ThreadPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SchedulerBuilder {
    static Logger logger = Logger.getLogger(SchedulerBuilder.class);
    @Autowired
    private JobStoreBuilder jobStoreBuilder;

    public Scheduler buildScheduler(String schedulerName, ThreadPool threadPool) throws SchedulerException {
        JobStore jobStore = jobStoreBuilder.buildJdbcJobStore(schedulerName);
        return buildScheduler(schedulerName, threadPool, jobStore, null, false);
    }

    public Scheduler buildScheduler(String schedulerName, ThreadPool threadPool, JobStore jobStore, Map<String, SchedulerPlugin> schedulerPluginMap, boolean jmxExport) throws SchedulerException {
        DirectSchedulerFactory schedulerFactory = DirectSchedulerFactory.getInstance();
        Scheduler scheduler = schedulerFactory.getScheduler(schedulerName);
        if (scheduler != null) {
            return scheduler;
        }
        schedulerFactory.createScheduler(
                schedulerName, "AUTO", threadPool, jobStore, schedulerPluginMap, null, 0, -1, -1, jmxExport, null);
        logger.info("create scheduler " + schedulerName + " success");
        return schedulerFactory.getScheduler(schedulerName);
    }
}
