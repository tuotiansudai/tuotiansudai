package com.tuotiansudai.scheduler.loan;

import com.google.common.collect.Lists;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.dto.sms.SmsCouponNotifyDto;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.repository.mapper.CouponMapper;
import com.tuotiansudai.repository.mapper.UserCouponMapper;
import com.tuotiansudai.repository.model.CouponModel;
import com.tuotiansudai.repository.model.UserCouponModel;
import com.tuotiansudai.repository.model.UserGroup;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.util.AmountConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.List;

@Component
public class CouponExpiredNotifyScheduler {
    static Logger logger = LoggerFactory.getLogger(CouponExpiredNotifyScheduler.class);

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Scheduled(cron = "0 0 9 * * ?", zone = "Asia/Shanghai")
    private void couponExpiredAfterFiveDays() {
        try {
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
                notifyDto.setRate(String.valueOf(couponModel.getRate() * 100));
                notifyDto.setCouponType(couponModel.getCouponType());
                notifyDto.setExpiredDate(new SimpleDateFormat("yyyy年MM月dd日").format(userCouponModel.getEndTime()));

                mqWrapperClient.sendMessage(MessageQueue.CouponSmsExpiredNotify, notifyDto);
            }
        } catch (Exception e) {
            logger.error("[CouponExpiredNotifyScheduler:] job execution is failed.", e);
        }

    }
}
