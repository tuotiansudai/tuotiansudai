package com.tuotiansudai.task.aspect;

import com.tuotiansudai.client.AbstractRedisWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.LoanDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.Role;
import com.tuotiansudai.service.AccountService;
import com.tuotiansudai.task.OperationTask;
import com.tuotiansudai.task.OperationType;
import com.tuotiansudai.task.TaskType;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Aspect
@Component
public class ApplicationAspect {

    @Autowired
    AbstractRedisWrapperClient redisWrapperClient;

    @Autowired
    AccountService accountService;

    public static final String TASK_KEY = "console:task:";

    public static final String NOTIFY_KEY = "console:notify:";

    static Logger logger = Logger.getLogger(ApplicationAspect.class);

    @Pointcut("execution(* com.tuotiansudai.service.LoanService.createLoan(..))")
    public void createLoanPointcut() {
    }

    @AfterReturning(value = "createLoanPointcut()", returning = "returnValue")
    public void afterReturningCreateLoan(JoinPoint joinPoint, Object returnValue) {
        logger.debug("after create loan");
        try {
            if (((BaseDto<PayDataDto>) returnValue).getData().getStatus()) {
                LoanDto loanDto = (LoanDto) joinPoint.getArgs()[0];

                OperationTask<LoanDto> task = new OperationTask<>();

                task.setTaskType(TaskType.TASK);
                task.setOperationType(OperationType.PROJECT);

                String taskId = task.getOperationType().toString() + "-" + loanDto.getId();
                task.setId(taskId);
                task.setObjId(String.valueOf(loanDto.getId()));
                task.setCreatedTime(new Date());

                String senderLoginName = loanDto.getCreatedLoginName();
                AccountModel sender = accountService.findByLoginName(senderLoginName);
                String senderRealName = sender.getUserName();

                task.setSender(senderLoginName);
                task.setOperateURL("/project-manage/loan/"+loanDto.getId());
                task.setDescription(senderRealName + "创建了新的标的，请审核。");

                redisWrapperClient.hsetSeri(TASK_KEY + Role.OPERATOR_ADMIN, String.valueOf(taskId), task);
            }
        } catch (Exception e) {
            logger.error("after create loan aspect fail ", e);
        }
    }


    @Pointcut("execution(* com.tuotiansudai.service.LoanService.openLoan(..))")
    public void openLoanPointcut() {
    }

    @AfterReturning(value = "openLoanPointcut()", returning = "returnValue")
    public void afterReturningOpenLoan(JoinPoint joinPoint, Object returnValue) {
        logger.debug("after open loan");
        try {
            LoanDto loanDto = (LoanDto) joinPoint.getArgs()[0];
            String taskId = OperationType.PROJECT + "-" + loanDto.getId();

            if (redisWrapperClient.hexistsSeri(TASK_KEY + Role.OPERATOR_ADMIN, taskId)) {
                redisWrapperClient.hdelSeri(TASK_KEY + Role.OPERATOR_ADMIN, taskId);

                OperationTask notify = new OperationTask();
                notify.setTaskType(TaskType.NOTIFY);
                notify.setOperationType(OperationType.PROJECT);
                notify.setSender(loanDto.getVerifyLoginName());
                notify.setReceiver(loanDto.getCreatedLoginName());
                notify.setCreatedTime(new Date());
                notify.setObjId(String.valueOf(loanDto.getId()));
                notify.setId(taskId);

                String senderLoginName = loanDto.getVerifyLoginName();
                AccountModel sender = accountService.findByLoginName(senderLoginName);
                String senderRealName = sender.getUserName();

                notify.setDescription(senderRealName + "通过了您的发标申请。");
                redisWrapperClient.hsetSeri(NOTIFY_KEY + loanDto.getCreatedLoginName(), taskId, notify);
            }
        } catch (Exception e) {
            logger.error("after open loan aspect fail ", e);
        }
    }
}
