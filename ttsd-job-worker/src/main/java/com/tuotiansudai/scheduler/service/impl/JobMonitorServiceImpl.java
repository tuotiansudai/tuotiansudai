package com.tuotiansudai.scheduler.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.Environment;
import com.tuotiansudai.dto.sms.SmsFatalNotifyDto;
import com.tuotiansudai.scheduler.repository.mapper.ExecutionLogMapper;
import com.tuotiansudai.scheduler.repository.model.ExecuteStatus;
import com.tuotiansudai.scheduler.repository.model.ExecutionLogModel;
import com.tuotiansudai.scheduler.service.JobMonitorService;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;

@Service
public class JobMonitorServiceImpl implements JobMonitorService {

    private static Logger logger = Logger.getLogger(JobMonitorServiceImpl.class);

    private static String JOB_LOG_ID_FIELD_NAME = "__job_log_id__";

    private ObjectMapper objectMapper = new ObjectMapper();

    @Value("${common.environment}")
    private Environment environment;

    private List<Class<?>> ignoredJobClasses;

    @Autowired
    private ExecutionLogMapper executionLogMapper;

    @Autowired
    private SmsWrapperClient smsWrapperClient;

    public JobMonitorServiceImpl() {
        ignoredJobClasses = Arrays.asList(
        );
    }

    @Override
    public void onJobToBeExecuted(JobExecutionContext context) {
        JobDetail jobDetail = context.getJobDetail();
        // 排除投资回调的job (执行太频繁，不适合记录执行情况)
        if (ignoredJobClasses.contains(jobDetail.getJobClass())) {
            return;
        }

        Trigger trigger = context.getTrigger();
        String schedulerName = "NO_NAME";
        try {
            Scheduler scheduler = context.getScheduler();
            schedulerName = scheduler.getSchedulerName();
        } catch (SchedulerException e) {
            logger.warn("cannot get scheduler name", e);
        }

        ExecutionLogModel logModel = new ExecutionLogModel();
        logModel.setSchedName(schedulerName);
        logModel.setTriggerName(trigger.getKey().getName());
        logModel.setTriggerGroup(trigger.getKey().getGroup());
        logModel.setJobName(jobDetail.getKey().getName());
        logModel.setJobGroup(jobDetail.getKey().getGroup());
        logModel.setFireTime(context.getFireTime().getTime());
        logModel.setDescription(jobDetail.getDescription());
        logModel.setJobClassName(jobDetail.getJobClass().getName());
        logModel.setExecuteStatus(ExecuteStatus.RUNNING);
        String jobData = null;
        try {
            jobData = objectMapper.writeValueAsString(jobDetail.getJobDataMap());
        } catch (JsonProcessingException e) {
            logger.warn("cannot parse jobData to json", e);
        }
        logModel.setJobData(jobData);
        executionLogMapper.create(logModel);
        jobDetail.getJobDataMap().put(JOB_LOG_ID_FIELD_NAME, logModel.getId());
    }

    @Override
    public void onJobExecuted(JobExecutionContext context, JobExecutionException exception) {
        JobDetail jobDetail = context.getJobDetail();
        Object objLogId = jobDetail.getJobDataMap().remove(JOB_LOG_ID_FIELD_NAME);
        // 排除没有记录的 log
        if (objLogId == null) {
            return;
        }
        long logId = (Long) objLogId;

        if (exception == null) {
            executionLogMapper.update(logId, ExecuteStatus.SUCCESS, "");
        } else {
            String exceptionData = ExceptionUtils.getStackTrace(exception);
            executionLogMapper.update(logId, ExecuteStatus.FAIL, exceptionData);

            JobKey jobKey = jobDetail.getKey();
            String jobParams = "-";
            try {
                jobParams = objectMapper.writeValueAsString(jobDetail.getJobDataMap());
            } catch (JsonProcessingException e) {
                logger.warn("cannot parse jobData to json", e);
            }
            String errorMessage = MessageFormat.format("{0},{1},{2},{3}", environment, jobKey.getGroup(), jobKey.getName(), jobParams);
            sendSmsFatalNotify(errorMessage);
        }
    }

    private void sendSmsFatalNotify(String errMsg) {
        logger.info("sent job fatal sms message");
        SmsFatalNotifyDto dto = new SmsFatalNotifyDto(MessageFormat.format("Job执行错误。详细信息：{0}", errMsg));
        smsWrapperClient.sendFatalNotify(dto);
    }
}
