package com.tuotiansudai.scheduler.loan;

import com.google.common.collect.Lists;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.dto.sms.SmsCouponNotifyDto;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.repository.mapper.CouponMapper;
import com.tuotiansudai.repository.mapper.UserCouponMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.CouponModel;
import com.tuotiansudai.repository.model.UserCouponModel;
import com.tuotiansudai.repository.model.UserGroup;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.util.AmountConverter;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class CouponExpiredNotifyScheduler {

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Scheduled(cron = "0 0 10 * * ?", zone = "Asia/Shanghai")
    private void couponExpiredAfterFiveDays() {
        final List<UserGroup> notifyUserGroups = Lists.newArrayList(UserGroup.IMPORT_USER, UserGroup.CHANNEL,
                UserGroup.FIRST_INVEST_ACHIEVEMENT, UserGroup.MAX_AMOUNT_ACHIEVEMENT, UserGroup.LAST_INVEST_ACHIEVEMENT);

        List<UserCouponModel> expireAfterFiveDays = userCouponMapper.findExpireAfterFiveDays();
        for (UserCouponModel userCouponModel : expireAfterFiveDays) {
            CouponModel couponModel = couponMapper.findById(userCouponModel.getCouponId());
            if (!notifyUserGroups.contains(couponModel.getUserGroup())) {
                continue;
            }

            UserModel userModel = userMapper.findByLoginName(userCouponModel.getLoginName());

            SmsCouponNotifyDto notifyDto = new SmsCouponNotifyDto();
            notifyDto.setMobile(userModel.getMobile());
            notifyDto.setAmount(AmountConverter.convertCentToString(couponModel.getAmount()));
            notifyDto.setRate(new BigDecimal(couponModel.getRate() * 100).setScale(0, BigDecimal.ROUND_UP).toString());
            notifyDto.setCouponType(couponModel.getCouponType());
            notifyDto.setExpiredDate(DateTime.now().plusDays(couponModel.getDeadline()).withTimeAtStartOfDay().toString("yyyy年MM月dd日"));

            mqWrapperClient.sendMessage(MessageQueue.CouponSmsExpiredNotify, notifyDto);
        }
    }
}
