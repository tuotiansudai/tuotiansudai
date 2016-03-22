package com.tuotiansudai.task.aspect;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.tuotiansudai.client.AbstractRedisWrapperClient;
import com.tuotiansudai.console.util.LoginUserInfo;
import com.tuotiansudai.dto.EditUserDto;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.mapper.UserRoleMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.Role;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserRoleModel;
import com.tuotiansudai.service.AccountService;
import com.tuotiansudai.service.AuditLogService;
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
public class AuditTaskAspectUser {

    @Autowired
    AbstractRedisWrapperClient redisWrapperClient;

    @Autowired
    AccountService accountService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private AuditLogService auditLogService;

    private static String DES_TEMPLATE = "\"loginName\":{0}, \"mobile\":{1}, \"email\":{2}, \"referrer\":{3}, \"status\":{4}, \"roles\":[{5}]";

    static Logger logger = Logger.getLogger(AuditTaskAspectUser.class);

    @Around(value = "execution(* com.tuotiansudai.service.UserService.editUser(..))")
    public Object aroundEditUser(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        logger.debug("around edit user aspect.");
        String operatorLoginName = (String) proceedingJoinPoint.getArgs()[0];
        EditUserDto editUserDto = (EditUserDto) proceedingJoinPoint.getArgs()[1];
        String ip = (String) proceedingJoinPoint.getArgs()[2];
        String taskId = OperationType.USER + "-" + editUserDto.getLoginName();
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
            notify.setObjId(editUserDto.getLoginName());
            notify.setId(taskId);
            notify.setSender(operatorLoginName);

            String receiverLoginName = task.getSender();
            notify.setReceiver(receiverLoginName);
            String senderRealName = accountService.getRealName(operatorLoginName);

            String editUserRealName = accountService.getRealName(editUserDto.getLoginName());
            notify.setDescription(senderRealName + " 通过了您修改用户［" + editUserRealName + "］的申请。");
            redisWrapperClient.hsetSeri(TaskConstant.NOTIFY_KEY + task.getSender(), taskId, notify);

            String receiverRealName = accountService.getRealName(receiverLoginName);
            String description = senderRealName + " 通过了 " + receiverRealName + " 修改用户［" + editUserRealName + "］的申请。";
            description += task.getDescription().split("的信息。")[1];
            auditLogService.createAuditLog(operatorLoginName, receiverLoginName, OperationType.USER, task.getObjId(), description, ip);

            return proceedingJoinPoint.proceed();
        } else {
            OperationTask<EditUserDto> task = new OperationTask<>();
            task.setTaskType(TaskType.TASK);
            task.setOperationType(OperationType.USER);
            task.setId(taskId);
            task.setObjId(editUserDto.getLoginName());

            String editUserRealName = StringUtils.isEmpty(editUserDto.getUserName()) ? editUserDto.getLoginName() : editUserDto.getUserName();
            task.setObjName(editUserRealName);
            task.setCreatedTime(new Date());
            task.setOperateURL("/user-manage/user/" + editUserDto.getLoginName());
            task.setSender(operatorLoginName);

            String senderRealName = accountService.getRealName(operatorLoginName);

            UserModel beforeUpdateUserModel = userMapper.findByLoginName(editUserDto.getLoginName());
            List<UserRoleModel> beforeUpdateUserRoleModels = userRoleMapper.findByLoginName(editUserDto.getLoginName());
            String beforeUpdate = getUserJsonString(beforeUpdateUserModel, beforeUpdateUserRoleModels);
            String afterUpdate = getUserJsonStringDto(editUserDto);
            task.setDescription(senderRealName + " 申请修改用户［" + editUserRealName + "］的信息。操作详情为：</br>" + "{" + beforeUpdate + "}" + " =></br> " + "{" + afterUpdate + "}");
            redisWrapperClient.hsetSeri(TaskConstant.TASK_KEY + Role.OPERATOR_ADMIN, taskId, task);
            return true;
        }
    }

    private String getUserJsonStringDto(EditUserDto editUserDto) {
        return MessageFormat.format(DES_TEMPLATE,
                "\"" + editUserDto.getLoginName() + "\"",
                "\"" + editUserDto.getMobile() + "\"",
                editUserDto.getEmail() != null ? "\"" + editUserDto.getEmail() + "\"" : "\"\"",
                editUserDto.getReferrer() != null ? "\"" + editUserDto.getReferrer() + "\"" : "\"\"",
                "\"" + editUserDto.getStatus().name() + "\"",
                Joiner.on(",").join(Lists.transform(editUserDto.getRoles(), new Function<Role, String>() {
                    @Override
                    public String apply(Role input) {
                        return "\"" + input.name() + "\"";
                    }
                })));
    }

    private String getUserJsonString(UserModel userModel, List<UserRoleModel> beforeUpdateUserRoleModels) {
        return MessageFormat.format(DES_TEMPLATE,
                "\"" + userModel.getLoginName() + "\"",
                "\"" + userModel.getMobile() + "\"",
                "\"" + userModel.getEmail() + "\"",
                "\"" + userModel.getReferrer() + "\"",
                "\"" + userModel.getStatus().name() + "\"",
                Joiner.on(",").join(Lists.transform(beforeUpdateUserRoleModels, new Function<UserRoleModel, String>() {
                    @Override
                    public String apply(UserRoleModel input) {
                        return "\"" + input.getRole().name() + "\"";
                    }
                })));
    }


}
