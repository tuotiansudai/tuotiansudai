package com.tuotiansudai.utils;

import com.tuotiansudai.utils.quartz.TriggeredJobBuilder;
import org.quartz.Job;
import org.quartz.Scheduler;
import org.springframework.beans.BeansException;

/**
 * example:
 * <pre>
 *     // set target job class
 *     Jobmanager.newjob(testjob.class)
 *
 *     // [optional] add parameters
 *     .addjobdata(some_parameters)
 *
 *     // [optional] add description
 *     .withdescription(some_parameters)
 *
 *     // [optional] add identity
 *     .withidentity(some_parameters)
 *
 *     // run once
 *     .runonceat(some_date)
 *
 *     // or run with schedule
 *     .runwithschedule(simpleschedulebuilder....)
 *
 *     // submit job to schedule
 *     .submit();
 * </pre>
 */
public class JobManager {
    public static TriggeredJobBuilder newJob(Class<? extends Job> jobClazz) {
        Scheduler scheduler = null;
        try {
            scheduler = SpringContextUtil.getBeanByType(Scheduler.class);
        }catch (BeansException exception){
        }
        return TriggeredJobBuilder.newJob(jobClazz, scheduler);
    }
}
