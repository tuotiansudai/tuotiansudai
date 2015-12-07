package com.tuotiansudai.scheduler.plugin;

import com.tuotiansudai.scheduler.listener.JobMonitorListener;
import org.apache.log4j.Logger;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.spi.ClassLoadHelper;
import org.quartz.spi.SchedulerPlugin;

public class JobMonitorPlugIn implements SchedulerPlugin {

    private static Logger logger = Logger.getLogger(JobMonitorPlugIn.class);

    private JobMonitorListener jobMonitorListener;

    public JobMonitorPlugIn(JobMonitorListener jobMonitorListener) {
        this.jobMonitorListener = jobMonitorListener;
    }

    @Override
    public void initialize(String name, Scheduler scheduler, ClassLoadHelper loadHelper) throws SchedulerException {
        try {
            scheduler.getListenerManager().addJobListener(jobMonitorListener);
        } catch (SchedulerException e) {
            logger.warn("init JobMonitorPlugin failed", e);
        }
    }

    @Override
    public void start() {
    }

    @Override
    public void shutdown() {
    }
}
