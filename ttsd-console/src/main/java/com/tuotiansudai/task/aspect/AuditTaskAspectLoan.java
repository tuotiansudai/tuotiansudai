package com.tuotiansudai.task.aspect;

import com.tuotiansudai.client.AbstractRedisWrapperClient;
import com.tuotiansudai.console.util.LoginUserInfo;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.LoanDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.repository.model.AccountModel;
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
public class AuditTaskAspectLoan {

    @Autowired
    AbstractRedisWrapperClient redisWrapperClient;

    @Autowired
    AccountService accountService;

    @Autowired
    private AuditLogService auditLogService;

    static Logger logger = Logger.getLogger(AuditTaskAspectLoan.class);

    @AfterReturning(value = "execution(* com.tuotiansudai.service.LoanService.createLoan(*))", returning = "returnValue")
    public void afterReturningCreateLoan(JoinPoint joinPoint, Object returnValue) {
        logger.debug("after create loan aspect.");
        try {
            if (((BaseDto<PayDataDto>) returnValue).getData().getStatus()) {
                LoanDto loanDto = (LoanDto) joinPoint.getArgs()[0];

                OperationTask<LoanDto> task = new OperationTask<>();

                task.setTaskType(TaskType.TASK);
                task.setOperationType(OperationType.PROJECT);

                String taskId = task.getOperationType().toString() + "-" + loanDto.getId();
                task.setId(taskId);
                task.setObjId(String.valueOf(loanDto.getId()));
                task.setObjName(loanDto.getProjectName());
                task.setCreatedTime(new Date());

                String senderLoginName = loanDto.getCreatedLoginName();
                AccountModel sender = accountService.findByLoginName(senderLoginName);
                String senderRealName = sender != null ? sender.getUserName() : senderLoginName;

                task.setSender(senderLoginName);
                task.setOperateURL("/project-manage/loan/" + loanDto.getId());
                task.setDescription(senderRealName + " 创建了新的标的［" + loanDto.getProjectName() + "］，请审核。");

                redisWrapperClient.hsetSeri(TaskConstant.TASK_KEY + Role.OPERATOR_ADMIN, String.valueOf(taskId), task);
            }
        } catch (Exception e) {
            logger.error("after create loan aspect fail ", e);
        }
    }


    @AfterReturning(value = "execution(* com.tuotiansudai.service.LoanService.openLoan(..))", returning = "returnValue")
    public void afterReturningOpenLoan(JoinPoint joinPoint, Object returnValue) {
        logger.debug("after open loan aspect.");
        try {
            if (((BaseDto<PayDataDto>) returnValue).getData().getStatus()) {
                LoanDto loanDto = (LoanDto) joinPoint.getArgs()[0];
                String ip = (String) joinPoint.getArgs()[1];
                String taskId = OperationType.PROJECT + "-" + loanDto.getId();

                if (redisWrapperClient.hexistsSeri(TaskConstant.TASK_KEY + Role.OPERATOR_ADMIN, taskId)) {

                    OperationTask task = (OperationTask) redisWrapperClient.hgetSeri(TaskConstant.TASK_KEY + Role.OPERATOR_ADMIN, taskId);

                    OperationTask notify = new OperationTask();
                    String notifyId = taskId;

                    notify.setId(notifyId);
                    notify.setTaskType(TaskType.NOTIFY);
                    notify.setOperationType(OperationType.PROJECT);

                    String senderLoginName = LoginUserInfo.getLoginName();
                    notify.setSender(senderLoginName);

                    String receiverLoginName = task.getSender();
                    notify.setReceiver(receiverLoginName);
                    notify.setCreatedTime(new Date());
                    notify.setObjId(task.getObjId());

                    AccountModel sender = accountService.findByLoginName(senderLoginName);
                    String senderRealName = sender != null ? sender.getUserName() : senderLoginName;

                    notify.setDescription(senderRealName + " 通过了您 " + OperationType.PROJECT.getDescription() + "［" + task.getObjName() + "］的申请。");

                    redisWrapperClient.hdelSeri(TaskConstant.TASK_KEY + Role.OPERATOR_ADMIN, taskId);
                    redisWrapperClient.hsetSeri(TaskConstant.NOTIFY_KEY + loanDto.getCreatedLoginName(), notifyId, notify);

                    String description = senderRealName + " 审核通过了标的［" + task.getObjName() + "］。";
                    auditLogService.createAuditLog(senderLoginName, receiverLoginName, OperationType.PROJECT, task.getObjId(), description, ip);
                }
            }
        } catch (Exception e) {
            logger.error("after open loan aspect fail ", e);
        }
    }
}
