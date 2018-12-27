package com.tuotiansudai.task.aspect;

import com.google.common.base.Strings;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.LoanCreateRequestDto;
import com.tuotiansudai.dto.LoanDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.enums.OperationType;
import com.tuotiansudai.enums.Role;
import com.tuotiansudai.log.service.AuditLogService;
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
public class AuditTaskAspectLoan {

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LoanService loanService;

    @Autowired
    private AuditLogService auditLogService;

    static Logger logger = Logger.getLogger(AuditTaskAspectLoan.class);

    @AfterReturning(value = "execution(* com.tuotiansudai.console.service.ConsoleLoanCreateService.applyAuditLoan(*))", returning = "returnValue")
    public void afterReturningCreateLoan(JoinPoint joinPoint, BaseDto<PayDataDto> returnValue) {
        logger.info("after create loan aspect.");
        try {
            if (returnValue.getData().getStatus()) {
                long loanId = (long) joinPoint.getArgs()[0];

                LoanModel loanModel = loanService.findLoanById(loanId);

                OperationTask<LoanDto> task = new OperationTask<>();

                task.setTaskType(TaskType.TASK);
                task.setOperationType(OperationType.PROJECT);

                String taskId = task.getOperationType().toString() + "-" + loanModel.getId();
                task.setId(taskId);
                task.setObjId(String.valueOf(loanModel.getId()));
                task.setObjName(loanModel.getName());
                task.setCreatedTime(new Date());

                String senderLoginName = LoginUserInfo.getLoginName();
                UserModel sender = userMapper.findByLoginName(senderLoginName);
                String senderRealName = sender != null && !Strings.isNullOrEmpty(sender.getUserName()) ? sender.getUserName() : senderLoginName;

                task.setSender(senderLoginName);
                task.setOperateURL("/project-manage/loan/" + loanModel.getId());
                task.setDescription(senderRealName + " 提交了新的标的［" + loanModel.getName() + "］，请审核。");

                redisWrapperClient.hsetSeri(TaskConstant.TASK_KEY + Role.OPERATOR_ADMIN, String.valueOf(taskId), task);
            }
        } catch (Exception e) {
            logger.error("after create loan aspect fail ", e);
        }
    }


    @AfterReturning(value = "execution(* com.tuotiansudai.console.service.ConsoleLoanCreateService.openLoan(..))", returning = "returnValue")
    public void afterReturningOpenLoan(JoinPoint joinPoint, BaseDto<PayDataDto> returnValue) {
        logger.info("after open loan aspect.");
        try {
            if (returnValue.getData().getStatus()) {
                LoanCreateRequestDto loanDto = (LoanCreateRequestDto) joinPoint.getArgs()[0];
                String ip = (String) joinPoint.getArgs()[1];
                long loanId = loanDto.getLoan().getId();
                String taskId = MessageFormat.format("{0}-{1}", OperationType.PROJECT.name(), String.valueOf(loanId));

                if (redisWrapperClient.hexistsSeri(TaskConstant.TASK_KEY + Role.OPERATOR_ADMIN, taskId)) {

                    OperationTask task = (OperationTask) redisWrapperClient.hgetSeri(TaskConstant.TASK_KEY + Role.OPERATOR_ADMIN, taskId);

                    OperationTask notify = new OperationTask();

                    notify.setId(taskId);
                    notify.setTaskType(TaskType.NOTIFY);
                    notify.setOperationType(OperationType.PROJECT);

                    String senderLoginName = LoginUserInfo.getLoginName();
                    notify.setSender(senderLoginName);

                    String receiverLoginName = task.getSender();
                    notify.setReceiver(receiverLoginName);
                    notify.setCreatedTime(new Date());
                    notify.setObjId(task.getObjId());

                    String senderRealName = userService.getRealName(senderLoginName);

                    notify.setDescription(senderRealName + " 通过了您 " + OperationType.PROJECT.getDescription() + "［" + task.getObjName() + "］的申请。");

                    redisWrapperClient.hdelSeri(TaskConstant.TASK_KEY + Role.OPERATOR_ADMIN, taskId);
                    redisWrapperClient.hsetSeri(TaskConstant.NOTIFY_KEY + loanService.findLoanById(loanId).getCreatedLoginName(), taskId, notify);

                    String description = senderRealName + " 审核通过了标的［" + task.getObjName() + "］。";
                    auditLogService.createAuditLog(senderLoginName, receiverLoginName, OperationType.PROJECT, task.getObjId(), description, ip);
                }
            }
        } catch (Exception e) {
            logger.error("after open loan aspect fail ", e);
        }
    }
}
