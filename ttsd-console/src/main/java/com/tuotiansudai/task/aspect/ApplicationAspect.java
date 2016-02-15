package com.tuotiansudai.task.aspect;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.tuotiansudai.client.AbstractRedisWrapperClient;
import com.tuotiansudai.console.util.LoginUserInfo;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.EditUserDto;
import com.tuotiansudai.dto.LoanDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.mapper.UserRoleMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.Role;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserRoleModel;
import com.tuotiansudai.service.AccountService;
import com.tuotiansudai.task.OperationTask;
import com.tuotiansudai.task.OperationType;
import com.tuotiansudai.task.TaskType;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

@Aspect
@Component
public class ApplicationAspect {

    @Autowired
    AbstractRedisWrapperClient redisWrapperClient;

    @Autowired
    AccountService accountService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    public static final String TASK_KEY = "console:task:";

    public static final String NOTIFY_KEY = "console:notify:";

    private static String DES_TEMPLATE = "'{'loginName:{0}, mobile:{1}, email:{2}, referrer:{3}, status:{4}, roles:[{5}]'}'";

    static Logger logger = Logger.getLogger(ApplicationAspect.class);

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
                task.setDescription(senderRealName + "创建了新的标的'" + loanDto.getProjectName() + "'，请审核。");

                redisWrapperClient.hsetSeri(TASK_KEY + Role.OPERATOR_ADMIN, String.valueOf(taskId), task);
            }
        } catch (Exception e) {
            logger.error("after create loan aspect fail ", e);
        }
    }


    @AfterReturning(value = "execution(* com.tuotiansudai.service.LoanService.openLoan(*))", returning = "returnValue")
    public void afterReturningOpenLoan(JoinPoint joinPoint, Object returnValue) {
        logger.debug("after open loan aspect.");
        try {
            if (((BaseDto<PayDataDto>) returnValue).getData().getStatus()) {
                LoanDto loanDto = (LoanDto) joinPoint.getArgs()[0];
                String taskId = OperationType.PROJECT + "-" + loanDto.getId();

                if (redisWrapperClient.hexistsSeri(TASK_KEY + Role.OPERATOR_ADMIN, taskId)) {

                    OperationTask task = (OperationTask) redisWrapperClient.hgetSeri(ApplicationAspect.TASK_KEY + Role.OPERATOR_ADMIN, taskId);

                    OperationTask notify = new OperationTask();
                    String notifyId = taskId;

                    notify.setId(notifyId);
                    notify.setTaskType(TaskType.NOTIFY);
                    notify.setOperationType(OperationType.PROJECT);

                    String senderLoginName = LoginUserInfo.getLoginName();
                    notify.setSender(senderLoginName);
                    notify.setReceiver(task.getSender());
                    notify.setCreatedTime(new Date());
                    notify.setObjId(task.getObjId());

                    AccountModel sender = accountService.findByLoginName(senderLoginName);
                    String senderRealName = sender != null ? sender.getUserName() : senderLoginName;

                    notify.setDescription(senderRealName + "通过了您" + OperationType.PROJECT.getDescription() + "'" + task.getObjName() + "'的申请。");

                    redisWrapperClient.hdelSeri(TASK_KEY + Role.OPERATOR_ADMIN, taskId);
                    redisWrapperClient.hsetSeri(NOTIFY_KEY + loanDto.getCreatedLoginName(), notifyId, notify);
                }
            }
        } catch (Exception e) {
            logger.error("after open loan aspect fail ", e);
        }
    }

    @Around(value = "execution(* com.tuotiansudai.service.UserService.editUser(..))")
    public Object aroundEditUser(ProceedingJoinPoint proceedingJoinPoint, JoinPoint joinPoint) throws Throwable {
        String operatorLoginName = (String)joinPoint.getArgs()[0];
        EditUserDto editUserDto = (EditUserDto)joinPoint.getArgs()[1];
        String taskId = OperationType.USER + "-" + editUserDto.getLoginName();
        if (redisWrapperClient.hexistsSeri(TASK_KEY + Role.OPERATOR_ADMIN, taskId)) {
            OperationTask<EditUserDto> task = (OperationTask<EditUserDto>)redisWrapperClient.hgetSeri(TASK_KEY + Role.OPERATOR_ADMIN, taskId);
            redisWrapperClient.hdelSeri(TASK_KEY + Role.OPERATOR_ADMIN, taskId);
            OperationTask notify = new OperationTask();
            notify.setTaskType(TaskType.NOTIFY);
            notify.setOperationType(OperationType.USER);
            notify.setCreatedTime(new Date());
            notify.setObjId(editUserDto.getLoginName());
            notify.setId(taskId);
            notify.setSender(operatorLoginName);
            notify.setReceiver(task.getSender());
            AccountModel sender = accountService.findByLoginName(operatorLoginName);
            String senderRealName = sender != null ? sender.getUserName() : operatorLoginName;
            notify.setDescription(senderRealName + "通过了您修改用户"+editUserDto.getLoginName()+"的申请。");
            redisWrapperClient.hsetSeri(NOTIFY_KEY + task.getSender(), taskId, notify);
            return proceedingJoinPoint.proceed();
        } else {
            OperationTask<EditUserDto> task = new OperationTask<>();
            task.setTaskType(TaskType.TASK);
            task.setOperationType(OperationType.USER);
            task.setId(taskId);
            task.setObjId(editUserDto.getLoginName());
            task.setCreatedTime(new Date());
            task.setOperateURL("/user-manage/user/" + editUserDto.getLoginName() + "/task");
            task.setSender(operatorLoginName);
            AccountModel sender = accountService.findByLoginName(operatorLoginName);
            String senderRealName = sender != null ? sender.getUserName() : operatorLoginName;
            UserModel beforeUpdateUserModel = userMapper.findByLoginName(editUserDto.getLoginName());
            List<UserRoleModel> beforeUpdateUserRoleModels = userRoleMapper.findByLoginName(editUserDto.getLoginName());
            String beforeUpdate = MessageFormat.format(DES_TEMPLATE,
                    beforeUpdateUserModel.getLoginName(),
                    beforeUpdateUserModel.getMobile(),
                    beforeUpdateUserModel.getEmail(),
                    beforeUpdateUserModel.getReferrer(),
                    beforeUpdateUserModel.getStatus().name(),
                    Joiner.on(",").join(Lists.transform(beforeUpdateUserRoleModels, new Function<UserRoleModel, String>() {
                        @Override
                        public String apply(UserRoleModel input) {
                            return input.getRole().name();
                        }
                    })));
            String afterUpdate = MessageFormat.format(DES_TEMPLATE,
                    editUserDto.getLoginName(),
                    editUserDto.getMobile(),
                    editUserDto.getEmail() != null ? editUserDto.getEmail() : "",
                    editUserDto.getReferrer() != null ? editUserDto.getReferrer() : "",
                    editUserDto.getStatus().name(),
                    Joiner.on(",").join(Lists.transform(editUserDto.getRoles(), new Function<Role, String>() {
                        @Override
                        public String apply(Role input) {
                            return input.name();
                        }
                    })));
            task.setDescription(senderRealName + "申请修改用户" + editUserDto.getLoginName() + "的信息。操作详情为：" + beforeUpdate + " => " + afterUpdate);
            redisWrapperClient.hsetSeri(TASK_KEY + Role.OPERATOR_ADMIN, taskId, task);
            return true;
        }
    }

}
