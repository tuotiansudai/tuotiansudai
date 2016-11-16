package com.tuotiansudai.task.aspect;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.dto.EditUserDto;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.mapper.UserRoleMapper;
import com.tuotiansudai.repository.model.Role;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserRoleModel;
import com.tuotiansudai.service.AuditLogService;
import com.tuotiansudai.service.UserService;
import com.tuotiansudai.spring.LoginUserInfo;
import com.tuotiansudai.task.OperationTask;
import com.tuotiansudai.task.OperationType;
import com.tuotiansudai.task.TaskConstant;
import com.tuotiansudai.task.TaskType;
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

@Aspect
@Component
public class AuditTaskAspectBandCard {

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private AuditLogService auditLogService;

    private static String DES_TEMPLATE = "\"loginName\":{0}, \"mobile\":{1}, \"email\":{2}, \"referrer\":{3}, \"status\":{4}, \"roles\":[{5}]";

    static Logger logger = Logger.getLogger(AuditTaskAspectBandCard.class);

    @Around(value = "execution(* com.tuotiansudai.service.BandCardManagerService.updateBankCard(..))")
    public Object aroundEditUser(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        logger.debug("around edit user aspect.");
        String operatorLoginName = (String) proceedingJoinPoint.getArgs()[0];
        long bankCardId = (long) proceedingJoinPoint.getArgs()[1];
        String ip = (String) proceedingJoinPoint.getArgs()[2];
        String taskId = OperationType.BAND_CARD + "-" + operatorLoginName;
        String loginName = LoginUserInfo.getLoginName();
        List<UserRoleModel> userRoleModels = userRoleMapper.findByLoginName(loginName);
        boolean flag = false;
        for (UserRoleModel userRoleModel : userRoleModels) {
            if (userRoleModel.getRole() == Role.OPERATOR_ADMIN || userRoleModel.getRole() == Role.ADMIN) {
                flag = true;
                break;
            }
        }
        if (redisWrapperClient.hexistsSeri(TaskConstant.TASK_KEY + Role.OPERATOR_ADMIN, taskId) && flag) {
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
//            description += task.getDescription().split("操作。")[1];
            auditLogService.createAuditLog(loginName, receiverLoginName, OperationType.BAND_CARD, task.getObjId(), description, ip);

            return proceedingJoinPoint.proceed();
        } else {
            OperationTask<EditUserDto> task = new OperationTask<>();
            task.setTaskType(TaskType.TASK);
            task.setOperationType(OperationType.USER);
            task.setId(taskId);
            task.setObjId(operatorLoginName);

            String editUserRealName = userService.getRealName(operatorLoginName);
            task.setObjName(editUserRealName);
            task.setCreatedTime(new Date());
            task.setOperateURL("/user-manage/bind-card");
            task.setSender(StringUtils.isEmpty(loginName) ? operatorLoginName : loginName);
            task.setObjId(String.valueOf(bankCardId));

            String senderRealName = userService.getRealName(StringUtils.isEmpty(loginName) ? operatorLoginName : loginName);
            task.setDescription(senderRealName + " 申请终止用户［" + editUserRealName + "］换卡操作。");
            redisWrapperClient.hsetSeri(TaskConstant.TASK_KEY + Role.OPERATOR_ADMIN, taskId, task);
            return "申请成功!";
        }
    }

}
