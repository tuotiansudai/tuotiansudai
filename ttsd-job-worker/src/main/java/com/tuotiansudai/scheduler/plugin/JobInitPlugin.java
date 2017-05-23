package com.tuotiansudai.scheduler.plugin;

import com.tuotiansudai.job.*;
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
        if (JobType.DragonBoatSendPKPrize.name().equalsIgnoreCase(schedulerName)) {
            createDragonBoatSendPKPrizeJob();
        }
    }

    @Override
    public void shutdown() {

    }

    private void createDragonBoatSendPKPrizeJob() {
        try {
            logger.info("[Dragon Boat] DragonBoatPKSendExperienceJob.endTime:" + DragonBoatPKSendExperienceJob.endTime);
            jobManager.newJob(JobType.DragonBoatSendPKPrize, DragonBoatPKSendExperienceJob.class)
                    .withIdentity(JobType.DragonBoatSendPKPrize.name(), JobType.DragonBoatSendPKPrize.name())
                    .replaceExistingJob(true)
                    .runOnceAt(DateTime.parse(DragonBoatPKSendExperienceJob.endTime, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate()).submit();
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }

}
