package com.tuotiansudai.task.aspect;

import com.tuotiansudai.activity.repository.dto.ActivityDto;
import com.tuotiansudai.activity.repository.model.ActivityStatus;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.enums.OperationType;
import com.tuotiansudai.enums.Role;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.message.AuditLogMessage;
import com.tuotiansudai.service.UserService;
import com.tuotiansudai.task.OperationTask;
import com.tuotiansudai.task.TaskConstant;
import com.tuotiansudai.task.TaskType;
import com.tuotiansudai.util.RedisWrapperClient;
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

    static Logger logger = Logger.getLogger(AuditTaskAspectLoan.class);

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    @Autowired
    private MQWrapperClient mqWrapperClient;
    @Autowired
    private UserService userService;

    @AfterReturning(value = "execution(* com.tuotiansudai.activity.service.ActivityService.saveOrUpdate(..))", returning = "returnValue")
    public void afterCreateEditRecheckActivity(JoinPoint joinPoint, Object returnValue) {
        logger.info("after create edit recheck activity aspect.");
        try {
            ActivityDto activityDto = (ActivityDto) joinPoint.getArgs()[0];
            ActivityStatus activityStatus = (ActivityStatus) joinPoint.getArgs()[1];
            String loginName = (String) joinPoint.getArgs()[2];
            String ip = (String) joinPoint.getArgs()[3];

            String realName = userService.getRealName(loginName);

            String description;

            String operator = loginName;
            String operatorRealName = userService.getRealName(operator);
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
                        String senderRealName = userService.getRealName(senderLoginName);

                        task.setSender(senderLoginName);
                        task.setOperateURL("/activity-manage/activity-center/" + activityDto.getActivityId());
                        task.setDescription(senderRealName + " 创建了一个活动［" + activityDto.getTitle() + "］，请审核。");

                        redisWrapperClient.hsetSeri(TaskConstant.TASK_KEY + Role.OPERATOR_ADMIN, String.valueOf(taskId), task);
                    }
                    if (activityDto.getActivityId() == null) {
                        description = realName + " 创建了活动［" + activityDto.getTitle() + "］。";
                    } else {
                        description = realName + " 编辑了活动［" + activityDto.getTitle() + "］。";
                    }
                    mqWrapperClient.sendMessage(MessageQueue.AuditLog, AuditLogMessage.createAuditLog(null, loginName, OperationType.ACTIVITY, String.valueOf(activityDto.getActivityId()), description, ip, userService.getMobile(loginName), ""));
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
                    mqWrapperClient.sendMessage(MessageQueue.AuditLog, AuditLogMessage.createAuditLog(null, loginName, OperationType.ACTIVITY, String.valueOf(activityDto.getActivityId()), description, ip, userService.getMobile(loginName), ""));
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
                    mqWrapperClient.sendMessage(MessageQueue.AuditLog, AuditLogMessage.createAuditLog(null, loginName, OperationType.ACTIVITY, String.valueOf(activityDto.getActivityId()), description, ip, userService.getMobile(loginName), ""));
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
