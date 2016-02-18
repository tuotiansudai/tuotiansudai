package com.tuotiansudai.task.aspect;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.tuotiansudai.client.AbstractRedisWrapperClient;
import com.tuotiansudai.console.jpush.dto.JPushAlertDto;
import com.tuotiansudai.console.util.LoginUserInfo;
import com.tuotiansudai.coupon.dto.CouponDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.EditUserDto;
import com.tuotiansudai.dto.LoanDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.mapper.UserRoleMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.AccountService;
import com.tuotiansudai.service.AuditLogService;
import com.tuotiansudai.task.OperationTask;
import com.tuotiansudai.task.OperationType;
import com.tuotiansudai.task.TaskConstant;
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
public class AuditTaskAspect {

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

    static Logger logger = Logger.getLogger(AuditTaskAspect.class);

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
                task.setDescription(senderRealName + "创建了新的标的［" + loanDto.getProjectName() + "］，请审核。");

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

                    notify.setDescription(senderRealName + "通过了您" + OperationType.PROJECT.getDescription() + "［" + task.getObjName() + "］的申请。");

                    redisWrapperClient.hdelSeri(TaskConstant.TASK_KEY + Role.OPERATOR_ADMIN, taskId);
                    redisWrapperClient.hsetSeri(TaskConstant.NOTIFY_KEY + loanDto.getCreatedLoginName(), notifyId, notify);

                    String description = senderRealName + "审核通过了标的［" + task.getObjName() + "］。" + "［ID：" + task.getObjId() + "］";
                    auditLogService.createAuditLog(senderLoginName, receiverLoginName, description, ip);
                }
            }
        } catch (Exception e) {
            logger.error("after open loan aspect fail ", e);
        }
    }

    @Around(value = "execution(* com.tuotiansudai.service.UserService.editUser(..))")
    public Object aroundEditUser(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
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
            AccountModel sender = accountService.findByLoginName(operatorLoginName);
            String senderRealName = sender != null ? sender.getUserName() : operatorLoginName;

            AccountModel account = accountService.findByLoginName(editUserDto.getLoginName());
            String editUserRealName = account != null ? account.getUserName() : editUserDto.getLoginName();
            notify.setDescription(senderRealName + "通过了您修改用户［" + editUserRealName + "］的申请。");
            redisWrapperClient.hsetSeri(TaskConstant.NOTIFY_KEY + task.getSender(), taskId, notify);

            String description = senderRealName + "通过了修改用户［" + task.getObjName() + "］的申请。";
            description += task.getDescription().split("的信息。")[1];
            auditLogService.createAuditLog(operatorLoginName, receiverLoginName, description, ip);

            return proceedingJoinPoint.proceed();
        } else {
            OperationTask<EditUserDto> task = new OperationTask<>();
            task.setTaskType(TaskType.TASK);
            task.setOperationType(OperationType.USER);
            task.setId(taskId);
            task.setObjId(editUserDto.getLoginName());
            task.setObjName(editUserDto.getUserName());
            task.setCreatedTime(new Date());
            task.setOperateURL("/user-manage/user/" + editUserDto.getLoginName());
            task.setSender(operatorLoginName);
            AccountModel sender = accountService.findByLoginName(operatorLoginName);
            String senderRealName = sender != null ? sender.getUserName() : operatorLoginName;
            UserModel beforeUpdateUserModel = userMapper.findByLoginName(editUserDto.getLoginName());
            List<UserRoleModel> beforeUpdateUserRoleModels = userRoleMapper.findByLoginName(editUserDto.getLoginName());
            String beforeUpdate = getUserJsonString(beforeUpdateUserModel, beforeUpdateUserRoleModels);
            String afterUpdate = getUserJsonStringDto(editUserDto);
            task.setDescription(senderRealName + "申请修改用户" + editUserDto.getLoginName() + "的信息。操作详情为：</br>" + "{" + beforeUpdate + "}" + " =></br> " + "{" + afterUpdate + "}");
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

    @AfterReturning(value = "execution(* com.tuotiansudai.coupon.service.CouponService.createCoupon(..))")
    public void afterReturningCreateCoupon(JoinPoint joinPoint) {
        logger.debug("after create coupon aspect.");
        try {
            String creator = (String) joinPoint.getArgs()[0];
            CouponDto couponDto = (CouponDto) joinPoint.getArgs()[1];

            couponAspect(creator, couponDto);
        } catch (Exception e) {
            logger.error("after create coupon aspect fail ", e);
        }
    }

    @AfterReturning(value = "execution(* com.tuotiansudai.coupon.service.CouponService.editCoupon(..))")
    public void afterReturningEditCoupon(JoinPoint joinPoint) {
        logger.debug("after edit coupon aspect.");
        try {
            String creator = (String) joinPoint.getArgs()[0];
            CouponDto couponDto = (CouponDto) joinPoint.getArgs()[1];

            couponAspect(creator, couponDto);
        } catch (Exception e) {
            logger.error("after edit coupon aspect fail ", e);
        }
    }

    private void couponAspect(String creator, CouponDto couponDto) {
        String taskId = OperationType.COUPON.toString() + "-" + couponDto.getId();

        if (!redisWrapperClient.hexistsSeri(TaskConstant.TASK_KEY + Role.OPERATOR_ADMIN, taskId)) {

            OperationTask<CouponDto> task = new OperationTask<>();

            task.setId(taskId);
            task.setTaskType(TaskType.TASK);
            task.setOperationType(OperationType.COUPON);
            task.setObjId(String.valueOf(couponDto.getId()));
            task.setObjName(couponDto.getCouponType().getName());
            task.setCreatedTime(new Date());

            String senderLoginName = creator;
            AccountModel sender = accountService.findByLoginName(senderLoginName);
            String senderRealName = sender != null ? sender.getUserName() : senderLoginName;

            task.setSender(senderLoginName);

            String operateURL;
            if (couponDto.getCouponType() == CouponType.INTEREST_COUPON) {
                operateURL = "/activity-manage/interest-coupons";
            } else if (couponDto.getCouponType() == CouponType.RED_ENVELOPE) {
                operateURL = "/activity-manage/red-envelopes";
            } else {
                operateURL = "/activity-manage/coupons";
            }

            task.setOperateURL(operateURL);
            task.setDescription(senderRealName + "创建了一张 " + couponDto.getCouponType().getName() + "，请审核。");

            redisWrapperClient.hsetSeri(TaskConstant.TASK_KEY + Role.OPERATOR_ADMIN, String.valueOf(taskId), task);
        }
    }

    @AfterReturning(value = "execution(* com.tuotiansudai.coupon.service.CouponActivationService.active(..))")
    public void afterReturningActiveCoupon(JoinPoint joinPoint) {
        logger.debug("after active coupon aspect.");
        try {
            String operator = (String) joinPoint.getArgs()[0];
            long couponId = (long) joinPoint.getArgs()[1];
            String ip = (String) joinPoint.getArgs()[2];
            String taskId = OperationType.COUPON + "-" + couponId;

            if (redisWrapperClient.hexistsSeri(TaskConstant.TASK_KEY + Role.OPERATOR_ADMIN, taskId)) {

                OperationTask task = (OperationTask) redisWrapperClient.hgetSeri(TaskConstant.TASK_KEY + Role.OPERATOR_ADMIN, taskId);

                OperationTask notify = new OperationTask();
                String notifyId = taskId;

                notify.setId(notifyId);
                notify.setTaskType(TaskType.NOTIFY);
                notify.setOperationType(OperationType.COUPON);

                String senderLoginName = operator;
                notify.setSender(senderLoginName);

                String receiverLoginName = task.getSender();
                notify.setReceiver(receiverLoginName);
                notify.setCreatedTime(new Date());
                notify.setObjId(task.getObjId());

                AccountModel sender = accountService.findByLoginName(senderLoginName);
                String senderRealName = sender != null ? sender.getUserName() : senderLoginName;

                notify.setDescription(senderRealName + "激活了您创建的" + task.getObjName() + "。");

                redisWrapperClient.hdelSeri(TaskConstant.TASK_KEY + Role.OPERATOR_ADMIN, taskId);
                redisWrapperClient.hsetSeri(TaskConstant.NOTIFY_KEY + task.getSender(), notifyId, notify);

                AccountModel receiver = accountService.findByLoginName(receiverLoginName);
                String receiverRealName = receiver != null ? receiver.getUserName() : receiverLoginName;
                String description = senderRealName + " 激活了 " + receiverRealName + " 创建的 " + task.getObjName() + "。［ID：" + task.getObjId() + "］";
                auditLogService.createAuditLog(senderLoginName, receiverLoginName, description, ip);
            }
        } catch (Exception e) {
            logger.error("after active coupon aspect fail ", e);
        }
    }

    @AfterReturning(value = "execution(* com.tuotiansudai.coupon.service.CouponService.deleteCoupon(..))")
    public void afterReturningDeleteCoupon(JoinPoint joinPoint) {
        logger.debug("after delete coupon aspect.");
        try {
            long couponId = (long) joinPoint.getArgs()[1];
            String taskId = OperationType.COUPON + "-" + couponId;
            redisWrapperClient.hdelSeri(TaskConstant.TASK_KEY + Role.OPERATOR_ADMIN, taskId);
        } catch (Exception e) {
            logger.error("after delete coupon aspect fail ", e);
        }
    }


    @AfterReturning(value = "execution(* com.tuotiansudai.console.jpush.service.JPushAlertService.buildJPushAlert(..))")
    public void afterReturningBuildJPush(JoinPoint joinPoint) {
        logger.debug("after build JPush aspect.");
        try {

            String creator = (String) joinPoint.getArgs()[0];
            JPushAlertDto jPushAlertDto = (JPushAlertDto) joinPoint.getArgs()[1];

            String taskId = OperationType.PUSH.toString() + "-" + jPushAlertDto.getId();

            if (!redisWrapperClient.hexistsSeri(TaskConstant.TASK_KEY + Role.OPERATOR_ADMIN, taskId)) {

                OperationTask<LoanDto> task = new OperationTask<>();

                task.setId(taskId);
                task.setTaskType(TaskType.TASK);
                task.setOperationType(OperationType.PUSH);
                task.setObjId(String.valueOf(jPushAlertDto.getId()));
                task.setObjName(jPushAlertDto.getName());
                task.setCreatedTime(new Date());

                String senderLoginName = creator;
                AccountModel sender = accountService.findByLoginName(senderLoginName);
                String senderRealName = sender != null ? sender.getUserName() : senderLoginName;

                task.setSender(senderLoginName);
                task.setOperateURL("/app-push-manage/manual-app-push-list");
                task.setDescription(senderRealName + "创建了一个APP推送［" + jPushAlertDto.getName() + "］，请审核。");

                redisWrapperClient.hsetSeri(TaskConstant.TASK_KEY + Role.OPERATOR_ADMIN, String.valueOf(taskId), task);
            }
        } catch (Exception e) {
            logger.error("after build JPush aspect fail ", e);
        }
    }

    @AfterReturning(value = "execution(* com.tuotiansudai.console.jpush.service.JPushAlertService.send(..))")
    public void afterReturningSendJPush(JoinPoint joinPoint) {
        logger.debug("after send JPush aspect.");
        try {
            String operator = (String) joinPoint.getArgs()[0];
            long jPushId = (long) joinPoint.getArgs()[1];
            String ip = (String) joinPoint.getArgs()[2];

            String taskId = OperationType.PUSH + "-" + jPushId;

            if (redisWrapperClient.hexistsSeri(TaskConstant.TASK_KEY + Role.OPERATOR_ADMIN, taskId)) {

                OperationTask task = (OperationTask) redisWrapperClient.hgetSeri(TaskConstant.TASK_KEY + Role.OPERATOR_ADMIN, taskId);

                OperationTask notify = new OperationTask();
                String notifyId = taskId;

                notify.setId(notifyId);
                notify.setTaskType(TaskType.NOTIFY);
                notify.setOperationType(OperationType.COUPON);

                String senderLoginName = operator;
                notify.setSender(senderLoginName);

                String receiverLoginName = task.getSender();
                notify.setReceiver(receiverLoginName);
                notify.setCreatedTime(new Date());
                notify.setObjId(task.getObjId());

                AccountModel sender = accountService.findByLoginName(senderLoginName);
                String senderRealName = sender != null ? sender.getUserName() : senderLoginName;

                notify.setDescription(senderRealName + "发送了您创建的APP推送［" + task.getObjName() + "］。");

                redisWrapperClient.hdelSeri(TaskConstant.TASK_KEY + Role.OPERATOR_ADMIN, taskId);
                redisWrapperClient.hsetSeri(TaskConstant.NOTIFY_KEY + task.getSender(), notifyId, notify);

                AccountModel receiver = accountService.findByLoginName(receiverLoginName);
                String receiverRealName = receiver != null ? receiver.getUserName() : receiverLoginName;
                String description = senderRealName + " 审核通过了 " + receiverRealName + " 创建的APP推送［" + task.getObjName() + "］。［ID：" + task.getObjId() + "］";
                auditLogService.createAuditLog(senderLoginName, receiverLoginName, description, ip);

            }
        } catch (Exception e) {
            logger.error("after send JPush aspect fail ", e);
        }
    }

}
