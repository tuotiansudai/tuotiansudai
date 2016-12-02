package com.tuotiansudai.paywrapper.aspect;

import com.google.common.base.Strings;
import com.tuotiansudai.activity.repository.mapper.NotWorkMapper;
import com.tuotiansudai.activity.repository.model.ActivityCategory;
import com.tuotiansudai.activity.repository.model.NotWorkModel;
import com.tuotiansudai.coupon.service.CouponAssignmentService;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanDetailsMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.LoanDetailsModel;
import com.tuotiansudai.repository.model.UserModel;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Date;

@Aspect
@Component
public class NotWorkAspect {

    final private static Logger logger = Logger.getLogger(NotWorkAspect.class);

    @Autowired
    UserMapper userMapper;

    @Autowired
    LoanDetailsMapper loanDetailsMapper;

    @Autowired
    NotWorkMapper notWorkMapper;

    @Autowired
    InvestMapper investMapper;

    @Autowired
    CouponAssignmentService couponAssignmentService;

    final private static String LOAN_ACTIVITY_DESCRIPTION = "加薪专享";

    final static private long PRIZE_COUPON_ID = 322L;

    final static private long PRIZE_COUPON_INVEST_LIMIT = 300000L;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.notWork.startTime}\")}")
    private Date activityStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.notWork.endTime}\")}")
    private Date activityEndTime;

    @AfterReturning(value = "execution(* *..InvestService.investSuccess(..))")
    public void afterInvest(JoinPoint joinPoint) {
        InvestModel investModel = (InvestModel) joinPoint.getArgs()[0];
        if (null == investModel) {
            logger.error("not work activity invest query null.");
            return;
        }
        if (investModel.getLoanId() == 1) {
            //体验标不参与活动
            return;
        }
        LoanDetailsModel loanDetailsModel = loanDetailsMapper.getByLoanId(investModel.getLoanId());
        if (null == loanDetailsModel) {
            logger.error("not work activity loanDetails query null.");
            return;
        }

        String loginName = investModel.getLoginName();
        long amount = investModel.getAmount();

        try {
            if (loanDetailsModel.isActivity() && loanDetailsModel.getActivityDesc().equals(LOAN_ACTIVITY_DESCRIPTION)) {
                userInvest(loginName, amount);
            }
        } catch (Exception e) {
            logger.error(MessageFormat.format("{0} don`t increase invest amount.", loginName, e));
        }

        try {
            UserModel userModel = userMapper.findByLoginName(loginName);
            if (!Strings.isNullOrEmpty(userModel.getReferrer())) {
                recommendedInvest(loginName, amount);
            }
        } catch (Exception e) {
            logger.error(MessageFormat.format("{0}`s referrer don`t increase recommend invest amount.", loginName, e));
        }
    }

    private interface UpdateModelProducer {
        NotWorkModel createAction(NotWorkModel notWorkModel);

        NotWorkModel updateAction(NotWorkModel notWorkModel);
    }

    private BaseDto<BaseDataDto> update(String loginName, UpdateModelProducer updateModelProducer) {
        if (new Date().before(activityStartTime) || new Date().after(activityEndTime)) {
            return new BaseDto<>(new BaseDataDto(false, "非活动时间"));
        }

        NotWorkModel notWorkModel = notWorkMapper.findByLoginName(loginName);
        if (null == notWorkModel) {
            UserModel userModel = userMapper.findByLoginName(loginName);
            if (null != userModel) {
                notWorkModel = new NotWorkModel(userModel.getLoginName(), userModel.getUserName(), userModel.getMobile(), false, ActivityCategory.NO_WORK_ACTIVITY);
                notWorkModel = updateModelProducer.createAction(notWorkModel);
                notWorkMapper.create(notWorkModel);
            } else {
                return new BaseDto<>(new BaseDataDto(false, "用户不存在"));
            }
        } else {
            notWorkModel = updateModelProducer.updateAction(notWorkModel);
            notWorkMapper.update(notWorkModel);
        }
        return new BaseDto<>(new BaseDataDto(true));
    }

    private BaseDto<BaseDataDto> userInvest(String loginName, long investAmount) {
        return update(loginName, new UpdateModelProducer() {
            @Override
            public NotWorkModel createAction(NotWorkModel notWorkModel) {
                notWorkModel.setInvestAmount(investAmount);
                logger.debug("not work ready to send coupon");
                if (investAmount >= PRIZE_COUPON_INVEST_LIMIT) {
                    couponAssignmentService.assign(loginName, PRIZE_COUPON_ID, null);
                    notWorkModel.setSendCoupon(true);
                    logger.debug("not work send coupon");
                }
                logger.debug("not work send coupon finish");
                return notWorkModel;
            }

            @Override
            public NotWorkModel updateAction(NotWorkModel notWorkModel) {
                notWorkModel.setInvestAmount(notWorkModel.getInvestAmount() + investAmount);
                logger.debug("not work ready to send coupon");
                if (!notWorkModel.isSendCoupon() && notWorkModel.getInvestAmount() >= PRIZE_COUPON_INVEST_LIMIT) {
                    couponAssignmentService.assign(loginName, PRIZE_COUPON_ID, null);
                    notWorkModel.setSendCoupon(true);
                    logger.debug("not work send coupon");
                }
                logger.debug("not work send coupon finish");
                return notWorkModel;
            }
        });
    }

    private BaseDto<BaseDataDto> recommendedInvest(String recommendedLoginName, long investAmount) {
        UserModel userModel = userMapper.findByLoginName(recommendedLoginName);
        if (userModel.getRegisterTime().before(activityStartTime) || userModel.getRegisterTime().after(activityEndTime)) {
            return new BaseDto<>(new BaseDataDto(false, "非活动期间注册用户"));
        }
        String referrerLoginName = userModel.getReferrer();
        if (Strings.isNullOrEmpty(referrerLoginName)) {
            return new BaseDto<>(new BaseDataDto(false, "无推荐人"));
        }
        return update(referrerLoginName, new UpdateModelProducer() {
            @Override
            public NotWorkModel createAction(NotWorkModel notWorkModel) {
                notWorkModel.setRecommendedInvestAmount(investAmount);
                return notWorkModel;
            }

            @Override
            public NotWorkModel updateAction(NotWorkModel notWorkModel) {
                notWorkModel.setRecommendedInvestAmount(notWorkModel.getRecommendedInvestAmount() + investAmount);
                return notWorkModel;
            }
        });
    }
}
