package com.tuotiansudai.scheduler.user;

import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.sms.SmsCouponNotifyDto;
import com.tuotiansudai.repository.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BirthdayNotifyScheduler {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SmsWrapperClient smsWrapperClient;

    @Scheduled(cron = "0 0 12 5 * ?", zone = "Asia/Shanghai")
    public void BirthdayNotify() {
        List<String> userMobileList = userMapper.findUsersBirthdayMobile();
        for (String mobile : userMobileList) {
            SmsCouponNotifyDto notifyDto = new SmsCouponNotifyDto();
            notifyDto.setMobile(mobile.trim());
            smsWrapperClient.sendBirthdayNotify(notifyDto);
        }
    }
}
