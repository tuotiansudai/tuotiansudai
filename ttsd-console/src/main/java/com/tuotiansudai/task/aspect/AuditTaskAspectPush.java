package com.tuotiansudai.task.aspect;

import com.tuotiansudai.client.AbstractRedisWrapperClient;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.LoanDto;
import com.tuotiansudai.jpush.dto.JPushAlertDto;
import com.tuotiansudai.repository.model.Role;
import com.tuotiansudai.service.AccountService;
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
public class AuditTaskAspectPush {

    @Autowired
    AbstractRedisWrapperClient redisWrapperClient;

    @Autowired
    AccountService accountService;

    static Logger logger = Logger.getLogger(AuditTaskAspectPush.class);

    @AfterReturning(value = "execution(* com.tuotiansudai.jpush.service.JPushAlertService.buildJPushAlert(..))")
    public void afterReturningBuildJPush(JoinPoint joinPoint) {
        logger.debug("after build JPush aspect.");
        try {

            String creator = (String) joinPoint.getArgs()[0];
            JPushAlertDto jPushAlertDto = (JPushAlertDto) joinPoint.getArgs()[1];

            String taskId = OperationType.PUSH.toString() + "-" + jPushAlertDto.getId();

            if (!redisWrapperClient.hexistsSeri(TaskConstant.TASK_KEY + Role.OPERATOR_ADMIN, taskId)) {

                OperationTask<LoanDto> task = new OperationTask<>();

                task.setId(taskId);
                task.setTaskType(TaskType.TASK);
                task.setOperationType(OperationType.PUSH);
                task.setObjId(String.valueOf(jPushAlertDto.getId()));
                task.setObjName(jPushAlertDto.getName());
                task.setCreatedTime(new Date());

                String senderLoginName = creator;
                String senderRealName = accountService.getRealName(senderLoginName);

                task.setSender(senderLoginName);
                task.setOperateURL("/app-push-manage/manual-app-push-list");
                task.setDescription(senderRealName + " 创建了一个APP推送［" + jPushAlertDto.getName() + "］，请审核。");

                redisWrapperClient.hsetSeri(TaskConstant.TASK_KEY + Role.OPERATOR_ADMIN, String.valueOf(taskId), task);
            }
        } catch (Exception e) {
            logger.error("after build JPush aspect fail ", e);
        }
    }

    @AfterReturning(value = "execution(* com.tuotiansudai.jpush.service.JPushAlertService.pass(..))", returning = "returnValue")
    public void afterReturningPassJPush(JoinPoint joinPoint, Object returnValue) {
        logger.debug("after pass JPush aspect.");
        try {
            if (((BaseDto<BaseDataDto>) returnValue).getData().getStatus()) {
                String operator = (String) joinPoint.getArgs()[0];
                long jPushId = (long) joinPoint.getArgs()[1];

                String taskId = OperationType.PUSH + "-" + jPushId;

                if (redisWrapperClient.hexistsSeri(TaskConstant.TASK_KEY + Role.OPERATOR_ADMIN, taskId)) {

                    OperationTask task = (OperationTask) redisWrapperClient.hgetSeri(TaskConstant.TASK_KEY + Role.OPERATOR_ADMIN, taskId);

                    OperationTask notify = getOperationTask(operator, taskId, task);

                    String senderRealName = accountService.getRealName(operator);

                    notify.setDescription(senderRealName + " 审核通过了您创建的APP推送［" + task.getObjName() + "］。");

                    redisWrapperClient.hdelSeri(TaskConstant.TASK_KEY + Role.OPERATOR_ADMIN, taskId);
                    redisWrapperClient.hsetSeri(TaskConstant.NOTIFY_KEY + task.getSender(), taskId, notify);
                }
            }
        } catch (Exception e) {
            logger.error("after pass JPush aspect fail ", e);
        }
    }

    @AfterReturning(value = "execution(* com.tuotiansudai.jpush.service.JPushAlertService.reject(..))")
    public void afterReturningRejectJPush(JoinPoint joinPoint) {
        logger.debug("after reject JPush aspect.");
        try {
            String operator = (String) joinPoint.getArgs()[0];
            long jPushId = (long) joinPoint.getArgs()[1];

            String taskId = OperationType.PUSH + "-" + jPushId;

            if (redisWrapperClient.hexistsSeri(TaskConstant.TASK_KEY + Role.OPERATOR_ADMIN, taskId)) {

                OperationTask task = (OperationTask) redisWrapperClient.hgetSeri(TaskConstant.TASK_KEY + Role.OPERATOR_ADMIN, taskId);

                OperationTask notify = getOperationTask(operator, taskId, task);

                String senderRealName = accountService.getRealName(operator);

                notify.setDescription(senderRealName + " 驳回了您创建的APP推送［" + task.getObjName() + "］。");

                redisWrapperClient.hdelSeri(TaskConstant.TASK_KEY + Role.OPERATOR_ADMIN, taskId);
                redisWrapperClient.hsetSeri(TaskConstant.NOTIFY_KEY + task.getSender(), taskId, notify);
            }
        } catch (Exception e) {
            logger.error("after reject JPush aspect fail ", e);
        }
    }

    @AfterReturning(value = "execution(* com.tuotiansudai.jpush.service.JPushAlertService.delete(..))")
    public void afterReturningDeleteJPush(JoinPoint joinPoint) {
        logger.debug("after delete JPush aspect.");
        try {
            long jPushId = (long) joinPoint.getArgs()[1];
            String taskId = OperationType.PUSH + "-" + jPushId;
            redisWrapperClient.hdelSeri(TaskConstant.TASK_KEY + Role.OPERATOR_ADMIN, taskId);
        } catch (Exception e) {
            logger.error("after delete JPush aspect fail ", e);
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
