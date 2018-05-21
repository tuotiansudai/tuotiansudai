package com.tuotiansudai.scheduler.loan;

import com.google.common.collect.Lists;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.dto.sms.SmsCouponNotifyDto;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.repository.mapper.CouponMapper;
import com.tuotiansudai.repository.mapper.UserCouponMapper;
import com.tuotiansudai.repository.model.*;
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

            List<UserCouponExpiredView> expireAfterTwoDays = userCouponMapper.findExpireAfterTwoDays();
            expireAfterTwoDays.forEach(expiredView -> {
                SmsCouponNotifyDto notifyDto = new SmsCouponNotifyDto();
                notifyDto.setMobile(expiredView.getMobile());
                notifyDto.setExpiredCount(expiredView.getExpiredCount());
                mqWrapperClient.sendMessage(MessageQueue.CouponSmsExpiredNotify, notifyDto);
            });

        } catch (Exception e) {
            logger.error("[CouponExpiredNotifyScheduler:] job execution is failed.", e);
        }

    }
}
