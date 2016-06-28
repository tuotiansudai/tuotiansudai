package com.tuotiansudai.task.aspect;


import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.dto.ActivityDto;
import com.tuotiansudai.repository.mapper.ActivityMapper;
import com.tuotiansudai.repository.model.ActivityModel;
import com.tuotiansudai.repository.model.ActivityStatus;
import com.tuotiansudai.repository.model.Role;
import com.tuotiansudai.service.AccountService;
import com.tuotiansudai.service.AuditLogService;
import com.tuotiansudai.task.OperationTask;
import com.tuotiansudai.task.OperationType;
import com.tuotiansudai.task.TaskConstant;
import com.tuotiansudai.task.TaskType;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Aspect
@Component
public class AuditTaskAspectActivity {


    @Autowired
    private AuditLogService auditLogService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    static Logger logger = Logger.getLogger(AuditTaskAspectLoan.class);

    @AfterReturning(value = "execution(* com.tuotiansudai.service.ActivityService.createEditRecheckActivity(..))", returning = "returnValue")
    public void afterCreateEditRecheckActivity(JoinPoint joinPoint, Object returnValue) {
        logger.debug("after create edit recheck activity aspect.");
        try {
            ActivityDto activityDto = (ActivityDto) joinPoint.getArgs()[0];
            ActivityStatus activityStatus = (ActivityStatus) joinPoint.getArgs()[1];
            String loginName = (String) joinPoint.getArgs()[2];
            String ip = (String) joinPoint.getArgs()[3];

            String realName = accountService.getRealName(loginName);

            String description;

            String operator = loginName;
            String operatorRealName = accountService.getRealName(operator);
            String taskId = OperationType.ACTIVITY.toString() + "-" + activityDto.getActivityId();
            switch (activityStatus) {
                case TO_APPROVE:
                    if (!redisWrapperClient.hexistsSeri(TaskConstant.TASK_KEY + Role.OPERATOR_ADMIN, taskId)) {
                        OperationTask<ActivityDto> task = new OperationTask<>();

                        task.setId(taskId);
                        task.setTaskType(TaskType.TASK);
                        task.setOperationType(OperationType.ACTIVITY);
                        task.setObjId(String.valueOf(activityDto.getActivityId()));
                        task.setObjName(activityDto.getTitle());
                        task.setCreatedTime(new Date());

                        String senderLoginName = operator;
                        String senderRealName = accountService.getRealName(senderLoginName);

                        task.setSender(senderLoginName);
                        task.setOperateURL("/activity-manage/activity-center/"+activityDto.getActivityId());
                        task.setDescription(senderRealName + " 创建了一个活动［" + activityDto.getTitle() + "］，请审核。");

                        redisWrapperClient.hsetSeri(TaskConstant.TASK_KEY + Role.OPERATOR_ADMIN, String.valueOf(taskId), task);
                    }
                    if (activityDto.getActivityId() == null) {
                        description = realName + " 创建了活动［" + activityDto.getTitle() + "］。";
                    } else {
                        description = realName + " 编辑了活动［" + activityDto.getTitle() + "］。";
                    }
                    auditLogService.createAuditLog(null, loginName, OperationType.ACTIVITY, String.valueOf(activityDto.getActivityId()), description, ip);
                    break;
                case REJECTION:

                    if (redisWrapperClient.hexistsSeri(TaskConstant.TASK_KEY + Role.OPERATOR_ADMIN, taskId)) {

                        OperationTask task = (OperationTask) redisWrapperClient.hgetSeri(TaskConstant.TASK_KEY + Role.OPERATOR_ADMIN, taskId);

                        OperationTask notify = getOperationTask(operator, taskId, task);

                        notify.setDescription(operatorRealName + " 驳回了您创建的活动［" + task.getObjName() + "］。");

                        redisWrapperClient.hdelSeri(TaskConstant.TASK_KEY + Role.OPERATOR_ADMIN, taskId);
                        redisWrapperClient.hsetSeri(TaskConstant.NOTIFY_KEY + task.getSender(), taskId, notify);
                    }

                    description = realName + " 驳回了 " + operatorRealName + " 创建的活动［" + activityDto.getTitle() + "］。";
                    auditLogService.createAuditLog(null, loginName, OperationType.ACTIVITY, String.valueOf(activityDto.getActivityId()), description, ip);
                    break;
                case APPROVED:
                    if (redisWrapperClient.hexistsSeri(TaskConstant.TASK_KEY + Role.OPERATOR_ADMIN, taskId)) {

                        OperationTask task = (OperationTask) redisWrapperClient.hgetSeri(TaskConstant.TASK_KEY + Role.OPERATOR_ADMIN, taskId);

                        OperationTask notify = getOperationTask(operator, taskId, task);

                        notify.setDescription(operatorRealName + " 审核通过了您创建的活动［" + task.getObjName() + "］。");

                        redisWrapperClient.hdelSeri(TaskConstant.TASK_KEY + Role.OPERATOR_ADMIN, taskId);
                        redisWrapperClient.hsetSeri(TaskConstant.NOTIFY_KEY + task.getSender(), taskId, notify);
                    }

                    description = realName + " 审核通过了 " + operatorRealName + " 创建的活动［" + activityDto.getTitle() + "］。";
                    auditLogService.createAuditLog(null, loginName, OperationType.ACTIVITY, String.valueOf(activityDto.getActivityId()), description, ip);
                    break;
                default:
                    throw new Exception("illegal activity status.");
            }
        } catch (Exception e) {
            logger.error("after create edit recheck activity aspect fail ", e);
        }
    }


    private OperationTask getOperationTask(String operator, String taskId, OperationTask task) {
        OperationTask notify = new OperationTask();
        notify.setId(taskId);
        notify.setTaskType(TaskType.NOTIFY);
        notify.setOperationType(OperationType.COUPON);
        notify.setSender(operator);
        notify.setReceiver(task.getSender());
        notify.setCreatedTime(new Date());
        notify.setObjId(task.getObjId());
        return notify;
    }
}
