package com.tuotiansudai.utils.quartz;


import com.tuotiansudai.utils.UUIDGenerator;
import org.apache.commons.lang3.StringUtils;
import org.quartz.*;

import java.util.Date;

/**
 * 可触发的Job构建器
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
public class TriggeredJobBuilder {
    private static final String DEFAULT_JOB_GROUP = "DEFAULT_JOB_GROUP_TTSD";
    private Scheduler scheduler;
    private Class<? extends Job> jobClazz;
    private JobDataMap jobDataMap;
    private JobKey jobKey;
    private TriggerKey triggerKey;
    private String jobDescription;
    private Date startDate;
    private ScheduleBuilder scheduleBuilder;

    public static TriggeredJobBuilder newJob(Class<? extends Job> jobClazz, Scheduler scheduler) {
        return new TriggeredJobBuilder(jobClazz, scheduler);
    }

    public TriggeredJobBuilder(Class<? extends Job> jobClazz, Scheduler scheduler) {
        this.scheduler = scheduler;
        this.jobClazz = jobClazz;
        this.jobDataMap = new JobDataMap();
    }

    public TriggeredJobBuilder setJobData(JobDataMap dataMap) {
        this.jobDataMap = dataMap;
        return this;
    }

    public TriggeredJobBuilder addJobData(JobDataMap dataMap) {
        this.jobDataMap.putAll(dataMap);
        return this;
    }

    public TriggeredJobBuilder addJobData(String key, Object value) {
        this.jobDataMap.put(key, value);
        return this;
    }

    public TriggeredJobBuilder withIdentity(String group, String name) {
        if (StringUtils.isBlank(group)) {
            group = DEFAULT_JOB_GROUP;
        }
        if (StringUtils.isBlank(name)) {
            name = UUIDGenerator.generate();
        }
        this.jobKey = JobKey.jobKey(name, group);
        this.triggerKey = TriggerKey.triggerKey(name, group);
        return this;
    }

    public TriggeredJobBuilder withDescription(String description) {
        this.jobDescription = jobDescription;
        return this;
    }

    public TriggeredJobBuilder runOnceAt(Date startDate) {
        this.startDate = startDate;
        return this;
    }

    public TriggeredJobBuilder runWithSchedule(ScheduleBuilder scheduleBuilder) {
        this.scheduleBuilder = scheduleBuilder;
        return this;
    }

    private JobDetail getJobDetail() {
        JobBuilder jobBuilder = JobBuilder.newJob(jobClazz).setJobData(jobDataMap);
        if (jobKey != null) {
            jobBuilder.withIdentity(jobKey);
        }
        if (StringUtils.isBlank(jobDescription)) {
            jobBuilder.withDescription(jobDescription);
        }
        JobDetail jobDetail = jobBuilder.build();
        return jobDetail;
    }

    private Trigger getJobTrigger(JobDetail jobDetail) {
        TriggerBuilder triggerBuilder = TriggerBuilder.newTrigger().withIdentity(triggerKey);
        if (scheduleBuilder != null) {
            triggerBuilder.withSchedule(scheduleBuilder);
        } else {
            triggerBuilder.withSchedule(SimpleScheduleBuilder.simpleSchedule());
            if (startDate == null) {
                triggerBuilder.startNow();
            } else {
                triggerBuilder.startAt(startDate);
            }
        }
        return triggerBuilder.build();
    }

    public void submit() throws SchedulerException {
        if (jobClazz == null) {
            throw new NullPointerException("jobClazz");
        }
        if (scheduler == null) {
            throw new NullPointerException("scheduler is null , please check quartz config");
        }
        JobDetail jobDetail = getJobDetail();
        Trigger jobTrigger = getJobTrigger(jobDetail);
        scheduler.scheduleJob(jobDetail, jobTrigger);
    }
}

