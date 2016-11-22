package com.tuotiansudai.activity.service;

import com.google.common.base.Strings;
import com.tuotiansudai.activity.repository.mapper.NotWorkMapper;
import com.tuotiansudai.activity.repository.model.NotWorkModel;
import com.tuotiansudai.coupon.service.CouponAssignmentService;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class NotWorkService {

    @Autowired
    NotWorkMapper notWorkMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    CouponAssignmentService couponAssignmentService;

    final static private long PRIZE_COUPON_ID = 322L;

    final static private long PRIZE_COUPON_INVEST_LIMIT = 300000L;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.notWork.startTime}\")}")
    private Date activityStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.notWork.endTime}\")}")
    private Date activityEndTime;

    final private long[] prizeList = {300000L, 800000L, 3000000L, 5000000L, 10000000L, 20000000L, 30000000L, 52000000L,
            80000000L, 120000000L};

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
                notWorkModel = new NotWorkModel(userModel.getLoginName(), userModel.getUserName(), userModel.getMobile(), false);
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

    public BaseDto<BaseDataDto> userInvest(String loginName, long investAmount) {
        return update(loginName, new UpdateModelProducer() {
            @Override
            public NotWorkModel createAction(NotWorkModel notWorkModel) {
                notWorkModel.setInvestAmount(investAmount);
                if (investAmount >= PRIZE_COUPON_INVEST_LIMIT) {
                    couponAssignmentService.assign(loginName, PRIZE_COUPON_ID, null);
                    notWorkModel.setSendCoupon(true);
                }
                return notWorkModel;
            }

            @Override
            public NotWorkModel updateAction(NotWorkModel notWorkModel) {
                notWorkModel.setInvestAmount(notWorkModel.getInvestAmount() + investAmount);
                if (!notWorkModel.isSendCoupon() && notWorkModel.getInvestAmount() >= PRIZE_COUPON_INVEST_LIMIT) {
                    couponAssignmentService.assign(loginName, PRIZE_COUPON_ID, null);
                    notWorkModel.setSendCoupon(true);
                }
                return notWorkModel;
            }
        });
    }

    public BaseDto<BaseDataDto> recommendedInvest(String recommendedLoginName, long investAmount) {
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

    public long getUsersActivityInvestAmount(String loginName) {
        NotWorkModel notWorkModel = notWorkMapper.findByLoginName(loginName);
        if (null == notWorkModel) {
            return 0L;
        } else {
            return notWorkModel.getInvestAmount();
        }
    }

    public long getUsersNeedInvestAmount(String loginName) {
        NotWorkModel notWorkModel = notWorkMapper.findByLoginName(loginName);
        if (null == notWorkModel) {
            return 0L;
        }
        for (long prize : prizeList) {
            if (prize > notWorkModel.getInvestAmount()) {
                return prize - notWorkModel.getInvestAmount();
            }
        }
        return 0L;
    }
}
