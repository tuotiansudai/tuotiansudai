package com.tuotiansudai.activity.service;

import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.util.RedisWrapperClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Date;

@Service
public class NewYearActivityService {

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.new.year.startTime}\")}")
    private Date activityStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.new.year.endTime}\")}")
    private Date activityEndTime;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    private RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    private static final String KEY = "NEW_YEAR_ACTIVITY_DRAW_COUPON:{0}";

    public String duringActivities() {
        if (new Date().before(activityStartTime)){
            return "NOT_START";
        }
        if (new Date().after(activityEndTime)){
            return "END";
        }
        return "START";
    }

    public boolean drewCoupon(String loginName) {
        return redisWrapperClient.exists(MessageFormat.format(KEY, loginName));
    }

    public void sendDrawCouponMessage(String loginName) {
        if ("START".equals(duringActivities())) {
            mqWrapperClient.sendMessage(MessageQueue.NewYearActivity_Coupon, loginName);
        }
    }
}










