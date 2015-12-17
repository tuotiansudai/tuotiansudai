package com.tuotiansudai.scheduler;

import com.google.common.collect.Maps;
import com.tuotiansudai.scheduler.listener.JobMonitorListener;
import com.tuotiansudai.scheduler.plugin.JobInitPlugin;
import com.tuotiansudai.scheduler.plugin.JobMonitorPlugIn;
import com.tuotiansudai.util.JobManager;
import com.tuotiansudai.util.quartz.AutowiringSpringBeanJobFactory;
import com.tuotiansudai.util.quartz.JobStoreBuilder;
import com.tuotiansudai.util.quartz.SchedulerBuilder;
import com.tuotiansudai.util.quartz.ThreadPoolBuilder;
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
public class Worker {

    static Logger logger = Logger.getLogger(Worker.class);

    @Autowired
    private AutowiringSpringBeanJobFactory jobFactory;

    @Autowired
    private SchedulerBuilder schedulerBuilder;

    @Autowired
    private JobStoreBuilder jobStoreBuilder;

    @Autowired
    private JobManager jobManager;

    @Autowired
    private JobMonitorListener jobMonitorListener;

    public void start() {
        logger.info("starting jobs");
        String[] schedulerNames = JobConfig.schedulerNames.trim().split("\\s*,\\s*");
        ThreadPool threadPool = ThreadPoolBuilder.buildThreadPool(JobConfig.threadCount, JobConfig.threadPriority);
        for (String schedulerName : schedulerNames) {
            logger.info("prepare scheduler for " + schedulerName);
            String fullSchedulerName = "Scheduler-" + schedulerName;
            JobStore jobStore = jobStoreBuilder.buildJdbcJobStore(
                    fullSchedulerName,
                    JobConfig.misfireThreshold, JobConfig.maxMisfiresToHandleAtATime,
                    JobConfig.isClustered, JobConfig.clusterCheckinInterval);
            logger.info("starting scheduler " + schedulerName);
            startScheduler(fullSchedulerName, threadPool, jobStore);
            logger.info("start scheduler " + schedulerName + " success");
        }
        logger.info("all schedulers has been started");
    }

    private void startScheduler(String schedulerName, ThreadPool threadPool, JobStore jobStore) {
        try {
            Map<String, SchedulerPlugin> schedulerPluginMap = Maps.newHashMap();
            schedulerPluginMap.put(JobMonitorPlugIn.class.getName(), new JobMonitorPlugIn(jobMonitorListener));
            schedulerPluginMap.put(JobInitPlugin.class.getName(), new JobInitPlugin(jobManager));

            Scheduler scheduler = schedulerBuilder.buildScheduler(schedulerName, threadPool, jobStore, schedulerPluginMap, true);
            scheduler.setJobFactory(jobFactory);
            scheduler.start();
        } catch (SchedulerException e) {
            logger.error("start schedulers : " + schedulerName + " failed", e);
        }
    }
}
