package com.tuotiansudai.utils;

import com.tuotiansudai.utils.quartz.TriggeredJobBuilder;
import org.quartz.Job;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * example:
 * <pre>
 *     // set target job class
 *     jobmanager.newjob(testjob.class)
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
@Component
public class JobManager {

    @Autowired
    private Scheduler scheduler;

    public TriggeredJobBuilder newJob(Class<? extends Job> jobClazz) {
        return TriggeredJobBuilder.newJob(jobClazz, scheduler);
    }
}
