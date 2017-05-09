package com.tuotiansudai.task.aspect;

import com.tuotiansudai.dto.CouponDto;
import com.tuotiansudai.enums.CouponType;
import com.tuotiansudai.enums.OperationType;
import com.tuotiansudai.enums.Role;
import com.tuotiansudai.repository.model.UserGroup;
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

import java.util.Date;

@Aspect
@Component
public class AuditTaskAspectCoupon {

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    @Autowired
    private UserService userService;

    static Logger logger = Logger.getLogger(AuditTaskAspectCoupon.class);

    @AfterReturning(value = "execution(* com.tuotiansudai.console.service.ConsoleCouponService.createCoupon(..))")
    public void afterReturningCreateCoupon(JoinPoint joinPoint) {
        logger.info("after create coupon aspect.");
        try {
            String creator = (String) joinPoint.getArgs()[0];
            CouponDto couponDto = (CouponDto) joinPoint.getArgs()[1];

            couponAspect(creator, couponDto);
        } catch (Exception e) {
            logger.error("after create coupon aspect fail ", e);
        }
    }

    @AfterReturning(value = "execution(* com.tuotiansudai.console.service.ConsoleCouponService.editCoupon(..))")
    public void afterReturningEditCoupon(JoinPoint joinPoint) {
        logger.info("after edit coupon aspect.");
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
            String senderRealName = userService.getRealName(senderLoginName);

            task.setSender(senderLoginName);

            String operateURL;
            if ((couponDto.getCouponType() == CouponType.RED_ENVELOPE || couponDto.getCouponType() == CouponType.INTEREST_COUPON || couponDto.getCouponType() == CouponType.INVEST_COUPON) && couponDto.getUserGroup() == UserGroup.EXCHANGER) {
                operateURL = "/point-manage/coupon-exchange-manage";
            } else {
                if (couponDto.getCouponType() == CouponType.INTEREST_COUPON) {
                    operateURL = "/activity-manage/interest-coupons";
                } else if (couponDto.getCouponType() == CouponType.RED_ENVELOPE) {
                    operateURL = "/activity-manage/red-envelopes";
                } else if (couponDto.getCouponType() == CouponType.BIRTHDAY_COUPON) {
                    operateURL = "/activity-manage/birthday-coupons";
                } else {
                    operateURL = "/activity-manage/coupons";
                }
            }
            task.setOperateURL(operateURL);
            task.setDescription(senderRealName + " 创建了一张 " + couponDto.getCouponType().getName() + "，请审核。");

            redisWrapperClient.hsetSeri(TaskConstant.TASK_KEY + Role.OPERATOR_ADMIN, String.valueOf(taskId), task);
        }
    }

    @AfterReturning(value = "execution(* com.tuotiansudai.console.service.CouponActivationService.active(..))")
    public void afterReturningActiveCoupon(JoinPoint joinPoint) {
        logger.info("after active coupon aspect.");
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

                String senderRealName = userService.getRealName(senderLoginName);
                notify.setDescription(senderRealName + " 激活了您创建的 " + task.getObjName() + "。");

                redisWrapperClient.hdelSeri(TaskConstant.TASK_KEY + Role.OPERATOR_ADMIN, taskId);
                redisWrapperClient.hsetSeri(TaskConstant.NOTIFY_KEY + task.getSender(), notifyId, notify);
            }
        } catch (Exception e) {
            logger.error("after active coupon aspect fail ", e);
        }
    }

    @AfterReturning(value = "execution(* com.tuotiansudai.console.service.ConsoleCouponService.deleteCoupon(..))")
    public void afterReturningDeleteCoupon(JoinPoint joinPoint) {
        logger.info("after delete coupon aspect.");
        try {
            long couponId = (long) joinPoint.getArgs()[1];
            String taskId = OperationType.COUPON + "-" + couponId;
            redisWrapperClient.hdelSeri(TaskConstant.TASK_KEY + Role.OPERATOR_ADMIN, taskId);
        } catch (Exception e) {
            logger.error("after delete coupon aspect fail ", e);
        }
    }
}
