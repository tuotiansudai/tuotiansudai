package com.tuotiansudai.scheduler.loan;

import com.google.common.collect.Lists;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.dto.sms.JianZhouSmsTemplate;
import com.tuotiansudai.dto.sms.SmsDto;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.repository.mapper.UserCouponMapper;
import com.tuotiansudai.repository.model.UserCouponExpiredView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CouponExpiredNotifyScheduler {
    static Logger logger = LoggerFactory.getLogger(CouponExpiredNotifyScheduler.class);

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Scheduled(cron = "0 0 9 * * ?", zone = "Asia/Shanghai")
    private void couponExpiredAfterFiveDays() {
        try {

            List<UserCouponExpiredView> expireAfterTwoDays = userCouponMapper.findExpireAfterTwoDays();
            expireAfterTwoDays.forEach(expiredView -> {
                mqWrapperClient.sendMessage(MessageQueue.UserSms, new SmsDto(JianZhouSmsTemplate.SMS_COUPON_EXPIRED_NOTIFY_TEMPLATE, Lists.newArrayList(expiredView.getMobile()), Lists.newArrayList()));
            });

        } catch (Exception e) {
            logger.error("[CouponExpiredNotifyScheduler:] job execution is failed.", e);
        }

    }
}
