package com.tuotiansudai.task.aspect;

import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.dto.EditUserDto;
import com.tuotiansudai.enums.OperationType;
import com.tuotiansudai.enums.Role;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.message.AuditLogMessage;
import com.tuotiansudai.repository.mapper.UserRoleMapper;
import com.tuotiansudai.repository.model.UserRoleModel;
import com.tuotiansudai.service.UserService;
import com.tuotiansudai.spring.LoginUserInfo;
import com.tuotiansudai.task.OperationTask;
import com.tuotiansudai.task.TaskConstant;
import com.tuotiansudai.task.TaskType;
import com.tuotiansudai.util.RedisWrapperClient;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Aspect
@Component
public class AuditTaskAspectBandCard {

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    @Autowired
    private UserService userService;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    public final String BAND_CARD_ACTIVE_STATUS_TEMPLATE = "bank_card_active_status";

    static Logger logger = Logger.getLogger(AuditTaskAspectBandCard.class);

    @Around(value = "execution(* com.tuotiansudai.service.BandCardManagerService.updateBankCard(..))")
    public Object aroundEditUser(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        logger.info("around edit user aspect.");
        String operatorLoginName = (String) proceedingJoinPoint.getArgs()[0];
        long bankCardId = (long) proceedingJoinPoint.getArgs()[1];
        String ip = (String) proceedingJoinPoint.getArgs()[2];
        String taskId = OperationType.BAND_CARD + "-" + operatorLoginName;
        String loginName = LoginUserInfo.getLoginName();

        List<UserRoleModel> userRoleModelList = userRoleMapper.findByLoginName(loginName);
        boolean isAdmin = Iterators.any(userRoleModelList.iterator(), input -> Role.OPERATOR_ADMIN.equals(input.getRole()) || Role.ADMIN.equals(input.getRole()));

        if (redisWrapperClient.hexistsSeri(TaskConstant.TASK_KEY + Role.OPERATOR_ADMIN, taskId) && isAdmin) {
            logger.info(MessageFormat.format("check bank card task aspect. user:{0}, bankCard:{1}", loginName, bankCardId));
            OperationTask<EditUserDto> task = (OperationTask<EditUserDto>) redisWrapperClient.hgetSeri(TaskConstant.TASK_KEY + Role.OPERATOR_ADMIN, taskId);
            redisWrapperClient.hdelSeri(TaskConstant.TASK_KEY + Role.OPERATOR_ADMIN, taskId);
            OperationTask notify = new OperationTask();
            notify.setTaskType(TaskType.NOTIFY);
            notify.setOperationType(OperationType.USER);
            notify.setCreatedTime(new Date());
            notify.setObjId(operatorLoginName);
            notify.setId(taskId);
            notify.setSender(operatorLoginName);

            String receiverLoginName = task.getSender();
            notify.setReceiver(receiverLoginName);
            String senderRealName = userService.getRealName(loginName);

            String editUserRealName = userService.getRealName(operatorLoginName);
            notify.setDescription(senderRealName + " 通过了您修改用户［" + editUserRealName + "］的申请。");
            redisWrapperClient.hsetSeri(TaskConstant.NOTIFY_KEY + task.getSender(), taskId, notify);

            String receiverRealName = userService.getRealName(receiverLoginName);
            String description = senderRealName + " 通过了 " + receiverRealName + " 修改用户［" + editUserRealName + "］的申请。";
            mqWrapperClient.sendMessage(MessageQueue.AuditLog, AuditLogMessage.createAuditLog(loginName, receiverLoginName, OperationType.BAND_CARD, task.getObjId(), description, ip, userService.getMobile(receiverLoginName), userService.getMobile(loginName)));
            redisWrapperClient.hdel(BAND_CARD_ACTIVE_STATUS_TEMPLATE, String.valueOf(bankCardId));

            return proceedingJoinPoint.proceed();
        } else {
            logger.info(MessageFormat.format("apply bank card task aspect. user:{0}, bankCard:{1}", loginName, bankCardId));
            OperationTask<EditUserDto> task = new OperationTask<>();
            task.setTaskType(TaskType.TASK);
            task.setOperationType(OperationType.USER);
            task.setId(taskId);
            task.setObjId(operatorLoginName);

            String editUserRealName = userService.getRealName(operatorLoginName);
            task.setObjName(editUserRealName);
            task.setCreatedTime(new Date());
            task.setOperateURL("/bank-card-manager/bind-card");
            task.setSender(StringUtils.isEmpty(loginName) ? operatorLoginName : loginName);
            task.setObjId(String.valueOf(bankCardId));
            task.setOperationType(OperationType.BAND_CARD);

            String senderRealName = userService.getRealName(StringUtils.isEmpty(loginName) ? operatorLoginName : loginName);
            task.setDescription(senderRealName + " 申请终止用户［" + editUserRealName + "］换卡操作。");
            redisWrapperClient.hsetSeri(TaskConstant.TASK_KEY + Role.OPERATOR_ADMIN, taskId, task);

            Map<String, String> bandCardApplyMap = Maps.newConcurrentMap();
            bandCardApplyMap.put(String.valueOf(bankCardId), "inRecheck");
            redisWrapperClient.hmset(BAND_CARD_ACTIVE_STATUS_TEMPLATE, bandCardApplyMap);

            return "申请成功,请等待运营管理员审核!";
        }
    }
}
