package com.tuotiansudai.task.aspect;

import com.google.common.collect.Lists;
import com.tuotiansudai.console.dto.PayrollDataDto;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.enums.OperationType;
import com.tuotiansudai.enums.Role;
import com.tuotiansudai.log.service.AuditLogService;
import com.tuotiansudai.repository.mapper.PayrollMapper;
import com.tuotiansudai.repository.mapper.UserRoleMapper;
import com.tuotiansudai.repository.model.PayrollModel;
import com.tuotiansudai.repository.model.PayrollStatusType;
import com.tuotiansudai.repository.model.UserRoleModel;
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

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

@Aspect
@Component
public class AuditTaskAspectPayroll {
    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    static Logger logger = Logger.getLogger(AuditTaskAspectPayroll.class);

    @Autowired
    private UserService userService;
    @Autowired
    private AuditLogService auditLogService;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private PayrollMapper payrollMapper;

    @AfterReturning(value = "execution(* com.tuotiansudai.console.service.ConsolePayrollService.createPayroll(..))" +
            "execution(* com.tuotiansudai.console.service.ConsolePayrollService.updatePayroll(..))")
    public void afterReturnCreatePayroll(JoinPoint joinPoint) {
        logger.info("after create payroll aspect.");
        try {
            String loginName = String.valueOf(joinPoint.getArgs()[0]);
            PayrollDataDto payrollDataDto = (PayrollDataDto) joinPoint.getArgs()[1];

            schedulingPayrollTask(payrollDataDto.getId(), loginName, pending);
        } catch (Exception e) {
            logger.error("after create payroll aspect fail ", e);
        }
        logger.info("after create payroll aspect.");
    }

    @AfterReturning(value = "execution(* com.tuotiansudai.console.service.ConsolePayrollService.primaryAudit(..))", returning = "returnValue")
    public void afterReturnPrimaryAudit(JoinPoint joinPoint, Object returnValue) {
        logger.info("after primaryAudit payroll aspect.");
        try {
            long payrollId = Long.valueOf(String.valueOf(joinPoint.getArgs()[0]));
            String loginName = String.valueOf(joinPoint.getArgs()[1]);
            BaseDto<BaseDataDto> baseDto = (BaseDto) returnValue;
            if (!baseDto.getData().getStatus()) {
                logger.debug(String.format("primaryAudit payroll aspect fail,error:%s", baseDto.getData().getMessage()));
                return;
            }

            schedulingPayrollTask(payrollId, loginName, audited);
        } catch (Exception e) {
            logger.error("after primaryAudit payroll aspect fail ", e);
        }
        logger.info("after primaryAudit payroll aspect.");
    }

    @AfterReturning(value = "execution(* com.tuotiansudai.console.service.ConsolePayrollService.finalAudit(..))", returning = "returnValue")
    public void afterReturnAdvancedAudit(JoinPoint joinPoint, Object returnValue) {
        logger.info("after finalAudit payroll aspect.");
        try {
            long payrollId = Long.valueOf(String.valueOf(joinPoint.getArgs()[0]));
            String loginName = String.valueOf(joinPoint.getArgs()[1]);

            BaseDto<BaseDataDto> baseDto = (BaseDto) returnValue;
            if (!baseDto.getData().getStatus()) {
                logger.debug(String.format("finalAudit payroll aspect fail,error:%s", baseDto.getData().getMessage()));
                return;
            }

            schedulingPayrollTask(payrollId, loginName, success);
        } catch (Exception e) {
            logger.error("after finalAudit payroll aspect fail ", e);
        }
        logger.info("after finalAudit payroll aspect.");
    }

    @AfterReturning(value = "execution(* com.tuotiansudai.console.service.ConsolePayrollService.reject(..))")
    public void afterReturnReject(JoinPoint joinPoint) {
        logger.info("after reject payroll aspect.");
        try {
            long payrollId = Long.valueOf(String.valueOf(joinPoint.getArgs()[0]));
            String loginName = String.valueOf(joinPoint.getArgs()[1]);

            schedulingPayrollTask(payrollId, loginName, rejected);
        } catch (Exception e) {
            logger.error("after reject payroll aspect fail ", e);
        }
        logger.info("after reject payroll aspect.");
    }

    Consumer<List<String>> pending = new Consumer<List<String>>() {
        @Override
        public void accept(List<String> list) {
            String loginName = list.get(0);
            String payrollId = list.get(1);
            String taskId = String.format("%s-%s", OperationType.PAYROLL.name(), payrollId);
            String operatorRealName = userService.getRealName(loginName);
            String description = String.format("%s提交了一份发放现金的申请，请审核。", operatorRealName);
            if (!redisWrapperClient.hexistsSeri(TaskConstant.TASK_KEY + Role.FINANCE_ADMIN, taskId)) {
                OperationTask<Long> task = new OperationTask(taskId, TaskType.TASK, OperationType.PAYROLL, loginName,
                        null, String.valueOf(payrollId), "代发工资",
                        description, String.format("/finance-manage/payroll-manage/%s/detail", String.valueOf(payrollId)));
                redisWrapperClient.hsetSeri(TaskConstant.TASK_KEY + Role.FINANCE_ADMIN, String.valueOf(taskId), task);
            }
            auditLogService.createAuditLog(null, loginName, OperationType.PAYROLL, String.valueOf(payrollId), description, null);
        }
    };

    Consumer<List<String>> audited = new Consumer<List<String>>() {
        @Override
        public void accept(List<String> list) {
            String loginName = list.get(0);
            String payrollId = list.get(1);
            String taskId = String.format("%s-%s", OperationType.PAYROLL.name(), payrollId);
            String description = "";
            String operatorRealName = userService.getRealName(loginName);
            if (redisWrapperClient.hexistsSeri(TaskConstant.TASK_KEY + Role.FINANCE_ADMIN, taskId)) {
                OperationTask task = (OperationTask) redisWrapperClient.hgetSeri(TaskConstant.TASK_KEY + Role.FINANCE_ADMIN, taskId);
                description = String.format("财务管理员%s通过了%s提交的发放现金申请，请进行二次审核", operatorRealName, task.getSender());
                OperationTask<Long> notify = new OperationTask(taskId, TaskType.NOTIFY, OperationType.PAYROLL, loginName,
                        task.getSender(), String.valueOf(payrollId), "代发工资",
                        String.format("财务管理员%s通过了您提交的发放现金申请，等待运营管理员审核", operatorRealName), null);
                redisWrapperClient.hdelSeri(TaskConstant.TASK_KEY + Role.FINANCE_ADMIN, taskId);
                redisWrapperClient.hsetSeri(TaskConstant.NOTIFY_KEY + task.getSender(), taskId, notify);

                task.setDescription(description);
                task.setSender(loginName);


                redisWrapperClient.hsetSeri(TaskConstant.TASK_KEY + Role.OPERATOR_ADMIN, String.valueOf(taskId), task);
            }

            auditLogService.createAuditLog(null, operatorRealName, OperationType.PAYROLL, String.valueOf(payrollId), description, null);
        }
    };

    Consumer<List<String>> success = new Consumer<List<String>>() {
        @Override
        public void accept(List<String> list) {
            String loginName = list.get(0);
            String payrollId = list.get(1);
            String taskId = String.format("%s-%s", OperationType.PAYROLL.name(), payrollId);
            String operatorRealName = userService.getRealName(loginName);
            if (redisWrapperClient.hexistsSeri(TaskConstant.TASK_KEY + Role.OPERATOR_ADMIN, taskId)) {

                OperationTask task = (OperationTask) redisWrapperClient.hgetSeri(TaskConstant.TASK_KEY + Role.OPERATOR_ADMIN, taskId);

                String description = String.format("运营管理员%s通过了你提交的发放现金申请。", operatorRealName);
                OperationTask<Long> notify = new OperationTask(taskId, TaskType.NOTIFY, OperationType.PAYROLL, loginName,
                        task.getSender(), task.getObjId(), task.getObjName(),
                        description, null);


                redisWrapperClient.hdelSeri(TaskConstant.TASK_KEY + Role.OPERATOR_ADMIN, taskId);
                redisWrapperClient.hsetSeri(TaskConstant.NOTIFY_KEY + task.getSender(), taskId, notify);
                auditLogService.createAuditLog(operatorRealName, task.getSender(), OperationType.PAYROLL, String.valueOf(payrollId), description, null);
            }
        }
    };

    Consumer<List<String>> rejected = new Consumer<List<String>>() {
        @Override
        public void accept(List<String> list) {
            String loginName = list.get(0);
            String payrollId = list.get(1);
            String taskId = String.format("%s-%s", OperationType.PAYROLL.name(), payrollId);
            String operatorRealName = userService.getRealName(loginName);
            Role currentRole = isFinanceAdmin(loginName) ? Role.FINANCE_ADMIN : Role.OPERATOR_ADMIN;
            if (redisWrapperClient.hexistsSeri(TaskConstant.TASK_KEY + currentRole, taskId)) {

                OperationTask task = (OperationTask) redisWrapperClient.hgetSeri(TaskConstant.TASK_KEY + currentRole, taskId);
                String description = isFinanceAdmin(loginName)
                        ? String.format("提交的发放现金的申请被财务管理员%s驳回", operatorRealName) : String.format("运营管理员%s通过了你提交的发放现金申请。", operatorRealName);
                OperationTask<Long> notify = new OperationTask(taskId, TaskType.NOTIFY, OperationType.PAYROLL, loginName,
                        task.getSender(), task.getObjId(), task.getObjName(),
                        description, null);

                redisWrapperClient.hdelSeri(TaskConstant.TASK_KEY + currentRole, taskId);
                redisWrapperClient.hsetSeri(TaskConstant.NOTIFY_KEY + task.getSender(), taskId, notify);
                if (!isFinanceAdmin(loginName)) {
                    PayrollModel payrollModel = payrollMapper.findById(Long.parseLong(payrollId));
                    String creator = userService.getRealName(payrollModel.getCreatedBy());
                    OperationTask<Long> creatorNotify = new OperationTask(taskId, TaskType.NOTIFY, OperationType.PAYROLL, loginName,
                            creator, task.getObjId(), task.getObjName(),
                            String.format("你提交的发放现金申请被运营管理员%s驳回", operatorRealName), null);
                    redisWrapperClient.hsetSeri(TaskConstant.NOTIFY_KEY + creator, taskId, creatorNotify);
                }
                auditLogService.createAuditLog(operatorRealName, task.getSender(), OperationType.PAYROLL, String.valueOf(payrollId), description, null);
            }
        }
    };

    private void schedulingPayrollTask(Long payrollId, String loginName, Consumer<List<String>> statusType) {
        List<String> param = Lists.newArrayList(loginName, String.valueOf(payrollId));
        statusType.accept(param);
    }

    private boolean isFinanceAdmin(String loginName) {
        List<UserRoleModel> userRoleModels = userRoleMapper.findByLoginName(loginName);
        return userRoleModels.stream().anyMatch(userRoleModel -> userRoleModel.getRole() == Role.FINANCE_ADMIN);
    }

}






