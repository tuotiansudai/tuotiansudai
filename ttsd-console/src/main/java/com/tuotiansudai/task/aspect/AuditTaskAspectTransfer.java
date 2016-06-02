package com.tuotiansudai.task.aspect;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.dto.TransferRuleDto;
import com.tuotiansudai.repository.mapper.UserRoleMapper;
import com.tuotiansudai.repository.model.Role;
import com.tuotiansudai.repository.model.UserRoleModel;
import com.tuotiansudai.service.AccountService;
import com.tuotiansudai.service.AuditLogService;
import com.tuotiansudai.task.OperationTask;
import com.tuotiansudai.task.OperationType;
import com.tuotiansudai.task.TaskConstant;
import com.tuotiansudai.task.TaskType;
import com.tuotiansudai.transfer.repository.mapper.TransferRuleMapper;
import com.tuotiansudai.transfer.repository.model.TransferRuleModel;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Date;

@Aspect
@Component
public class AuditTaskAspectTransfer {

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Autowired
    private TransferRuleMapper transferRuleMapper;

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private AuditLogService auditLogService;

    private final static String AUDIT_LOG_TEMPLATE = "持有30天以内转让手续费:{0}, 持有30天-90天转让手续费:{1}, 持有90天以上转让手续费:{2}, 转让折价金额:{3}, 回款前不可转让天数:{4}, 允许多次转让:{5}";

    static Logger logger = Logger.getLogger(AuditTaskAspectTransfer.class);

    @SuppressWarnings(value = "unchecked")
    @Around(value = "execution(* com.tuotiansudai.transfer.service.TransferRuleService.updateTransferRule(..))")
    public Object aroundUpdateTransferRule(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        logger.debug("update transfer rule task aspect starting");

        TransferRuleDto transferRuleDto = (TransferRuleDto) proceedingJoinPoint.getArgs()[0];
        String operator = (String) proceedingJoinPoint.getArgs()[1];
        String ip = (String) proceedingJoinPoint.getArgs()[2];

        boolean isOperatorAdmin = Iterators.tryFind(userRoleMapper.findByLoginName(operator).iterator(), new Predicate<UserRoleModel>() {
            @Override
            public boolean apply(UserRoleModel input) {
                return Lists.newArrayList(Role.ADMIN, Role.OPERATOR_ADMIN).contains(input.getRole());
            }
        }).isPresent();

        String taskKey = TaskConstant.TASK_KEY + Role.OPERATOR_ADMIN;
        String taskId = OperationType.TRANSFER_RULE.name();

        if (!redisWrapperClient.hexistsSeri(taskKey,  taskId)) {
            OperationTask<TransferRuleDto> task = new OperationTask<>();
            task.setId(taskId);
            task.setTaskType(TaskType.TASK);
            task.setOperationType(OperationType.TRANSFER_RULE);
            task.setObjName(OperationType.TRANSFER_RULE.getDescription());
            task.setCreatedTime(new Date());
            task.setObj(transferRuleDto);
            task.setOperateURL("/transfer-manage/transfer-rule");
            task.setSender(operator);

            TransferRuleModel transferRuleModel = transferRuleMapper.find();
            String beforeUpdate = MessageFormat.format(AUDIT_LOG_TEMPLATE,
                    String.valueOf(transferRuleModel.getLevelOneFee()),
                    String.valueOf(transferRuleModel.getLevelTwoFee()),
                    String.valueOf(transferRuleModel.getLevelThreeFee()),
                    String.valueOf(transferRuleModel.getDiscount()),
                    String.valueOf(transferRuleModel.getDaysLimit()),
                    String.valueOf(transferRuleModel.isMultipleTransferEnabled()));
            String afterUpdate = MessageFormat.format(AUDIT_LOG_TEMPLATE,
                    String.valueOf(transferRuleModel.getLevelOneFee()),
                    String.valueOf(transferRuleDto.getLevelTwoFee()),
                    String.valueOf(transferRuleDto.getLevelThreeFee()),
                    String.valueOf(transferRuleDto.getDiscount()),
                    String.valueOf(transferRuleDto.getDaysLimit()),
                    String.valueOf(transferRuleDto.isMultipleTransferEnabled()));

            task.setDescription(MessageFormat.format("{0} 修改债权转让规则 [{1}] => [{2}]", accountService.getRealName(operator), beforeUpdate, afterUpdate));
            redisWrapperClient.hsetSeri(taskKey, taskId, task);
            return true;
        }


        if (isOperatorAdmin) {
            proceedingJoinPoint.proceed();

            OperationTask<TransferRuleDto> task = (OperationTask<TransferRuleDto>) redisWrapperClient.hgetSeri(taskKey, taskId);
            redisWrapperClient.hdelSeri(taskKey, taskId);

            OperationTask<TransferRuleDto> notify = new OperationTask();
            notify.setId(taskId);
            notify.setTaskType(TaskType.NOTIFY);
            notify.setOperationType(OperationType.TRANSFER_RULE);
            notify.setCreatedTime(new Date());
            notify.setSender(operator);
            notify.setReceiver(task.getSender());
            String senderRealName = accountService.getRealName(operator);
            notify.setDescription(MessageFormat.format("{0} 通过了您修改债权转让规则的申请。", senderRealName));
            redisWrapperClient.hsetSeri(TaskConstant.NOTIFY_KEY + task.getSender(), taskId, notify);
            auditLogService.createAuditLog(operator, task.getSender(), OperationType.TRANSFER_RULE, task.getObjId(),
                    MessageFormat.format("{0} 通过了 {1}", senderRealName, task.getDescription()), ip);
        }

        return true;
    }
}
