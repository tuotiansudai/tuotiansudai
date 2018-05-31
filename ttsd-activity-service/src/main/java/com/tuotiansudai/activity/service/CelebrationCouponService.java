package com.tuotiansudai.activity.service;

import com.tuotiansudai.activity.repository.mapper.CelebrationDrawCouponMapper;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.message.InvestSuccessMidSummerMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CelebrationCouponService {

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.celebration.drawCoupon.startTime}\")}")
    private Date activityStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.celebration.drawCoupon.endTime}\")}")
    private Date activityEndTime;

    @Autowired
    private CelebrationDrawCouponMapper celebrationDrawCouponMapper;

    @Autowired
    private MQWrapperClient mqWrapperClient;


    public boolean duringActivities() {
        return activityStartTime.before(new Date()) && activityEndTime.after(new Date());
    }

    public boolean drewCoupon(String loginName) {
        return celebrationDrawCouponMapper.findByLoginName(loginName) != null;
    }

    public void sendDrawCouponMessage(String loginName) {
    }


}










