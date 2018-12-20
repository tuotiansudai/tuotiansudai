package com.tuotiansudai.task.aspect;

import com.google.common.base.Strings;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.enums.OperationType;
import com.tuotiansudai.enums.Role;
import com.tuotiansudai.log.service.AuditLogService;
import com.tuotiansudai.repository.mapper.LoanApplicationMapper;
import com.tuotiansudai.repository.model.LoanApplicationModel;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.service.LoanService;
import com.tuotiansudai.service.UserService;
import com.tuotiansudai.spring.LoginUserInfo;
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

import java.text.MessageFormat;
import java.util.Date;

@Aspect
@Component
public class AuditTaskAspectLoanApplication {

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LoanApplicationMapper loanApplicationMapper;

    @Autowired
    private AuditLogService auditLogService;

    static Logger logger = Logger.getLogger(AuditTaskAspectLoanApplication.class);

    @AfterReturning(value = "execution(* com.tuotiansudai.console.service.ConsoleLoanApplicationService.applyAuditLoanApplication(*))", returning = "returnValue")
    public void afterReturningCreateLoan(JoinPoint joinPoint, BaseDto<PayDataDto> returnValue) {
        logger.info("after apply loan application aspect.");
        try {
            if (returnValue.getData().getStatus()) {
                long loanApplicationId = (long) joinPoint.getArgs()[0];
                String senderLoginName = (String) joinPoint.getArgs()[1];

                LoanApplicationModel loanApplicationModel = loanApplicationMapper.findById(loanApplicationId);

                OperationTask<LoanApplicationDto> task = new OperationTask<>();

                task.setTaskType(TaskType.TASK);
                task.setOperationType(OperationType.CONSUME_LOAN);

                String taskId = task.getOperationType().toString() + "-" + loanApplicationModel.getId();
                task.setId(taskId);
                task.setObjId(String.valueOf(loanApplicationModel.getId()));
                task.setObjName("消费借款" + loanApplicationId);
                task.setCreatedTime(new Date());

                UserModel sender = userMapper.findByLoginName(senderLoginName);
                String senderRealName = sender != null && !Strings.isNullOrEmpty(sender.getUserName()) ? sender.getUserName() : senderLoginName;

                task.setSender(senderLoginName);
                task.setOperateURL("/loan-application/consume/" + loanApplicationModel.getId());
                task.setDescription(senderRealName + " 提交了新的消费借款，请审核。");

                redisWrapperClient.hsetSeri(TaskConstant.TASK_KEY + Role.RISK_CONTROL_STAFF, String.valueOf(taskId), task);
            }
        } catch (Exception e) {
            logger.error("after apply loan application aspect fail ", e);
        }
    }

    @AfterReturning(value = "execution(* com.tuotiansudai.console.service.ConsoleLoanApplicationService.consumeApprove(..))", returning = "returnValue")
    public void afterReturningOpenLoan(JoinPoint joinPoint, BaseDto<PayDataDto> returnValue) {
        logger.info("after approve loan application aspect.");
        try {
            if (returnValue.getData().getStatus()) {
                long loanApplicationId = (long) joinPoint.getArgs()[0];
                String ip = (String) joinPoint.getArgs()[1];
                String taskId = MessageFormat.format("{0}-{1}", OperationType.CONSUME_LOAN.name(), String.valueOf(loanApplicationId));

                if (redisWrapperClient.hexistsSeri(TaskConstant.TASK_KEY + Role.RISK_CONTROL_STAFF, taskId)) {

                    OperationTask task = (OperationTask) redisWrapperClient.hgetSeri(TaskConstant.TASK_KEY + Role.RISK_CONTROL_STAFF, taskId);

                    OperationTask notify = new OperationTask();

                    notify.setId(taskId);
                    notify.setTaskType(TaskType.NOTIFY);
                    notify.setOperationType(OperationType.CONSUME_LOAN);

                    String senderLoginName = LoginUserInfo.getLoginName();
                    notify.setSender(senderLoginName);

                    String receiverLoginName = task.getSender();
                    notify.setReceiver(receiverLoginName);
                    notify.setCreatedTime(new Date());
                    notify.setObjId(task.getObjId());

                    String senderRealName = userMapper.findByLoginName(senderLoginName).getUserName();

                    notify.setDescription(senderRealName + " 通过了您 " + OperationType.CONSUME_LOAN.getDescription() + "［" + task.getObjName() + "］的申请。");

                    redisWrapperClient.hdelSeri(TaskConstant.TASK_KEY + Role.RISK_CONTROL_STAFF, taskId);
//                    redisWrapperClient.hsetSeri(TaskConstant.NOTIFY_KEY + loanService.findLoanById(loanId).getCreatedLoginName(), taskId, notify);

                    String description = senderRealName + " 审核通过了消费借款 编号［" + loanApplicationId + "］。";
                    auditLogService.createAuditLog(senderLoginName, receiverLoginName, OperationType.CONSUME_LOAN, task.getObjId(), description, ip);
                }
            }
        } catch (Exception e) {
            logger.error("after open loan aspect fail ", e);
        }
    }
}
