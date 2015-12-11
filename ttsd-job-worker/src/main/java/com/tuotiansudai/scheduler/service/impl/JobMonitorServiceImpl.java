package com.tuotiansudai.scheduler.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.SmsJobFatalNotifyDto;
import com.tuotiansudai.job.InvestCallback;
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
import java.util.List;

@Service
public class JobMonitorServiceImpl implements JobMonitorService {

    private static Logger logger = Logger.getLogger(JobMonitorServiceImpl.class);

    private static String JOB_LOG_ID_FIELD_NAME = "__job_log_id__";

    private ObjectMapper objectMapper = new ObjectMapper();

    @Value("#{'${pay.invest.notify.fatal.mobile}'.split('\\|')}")
    private List<String> fatalNotifyMobiles;

    @Autowired
    private ExecutionLogMapper executionLogMapper;

    @Autowired
    private SmsWrapperClient smsWrapperClient;

    @Override
    public void onJobToBeExecuted(JobExecutionContext context) {
        JobDetail jobDetail = context.getJobDetail();
        // 排除投资回调的job (执行太频繁，不适合记录执行情况)
        if (InvestCallback.class.equals(jobDetail.getJobClass())) {
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
            String errorMessage = MessageFormat.format("{0}-{1},params:{2}", jobKey.getGroup(), jobKey.getName(), jobParams);
            sendSmsErrNotify(fatalNotifyMobiles, errorMessage);
        }
    }

    private void sendSmsErrNotify(List<String> mobiles, String errMsg) {
        for (String mobile : mobiles) {
            logger.info("sent job fatal sms message to " + mobile);

            SmsJobFatalNotifyDto dto = new SmsJobFatalNotifyDto();
            dto.setMobile(mobile);
            dto.setErrMsg(errMsg);
            smsWrapperClient.sendJobFatalNotify(dto);
        }
    }
}
