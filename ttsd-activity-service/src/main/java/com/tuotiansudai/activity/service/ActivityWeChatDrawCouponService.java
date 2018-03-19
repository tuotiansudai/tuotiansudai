package com.tuotiansudai.activity.service;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.activity.repository.model.ActivityCategory;
import com.tuotiansudai.activity.repository.model.ActivityWeChatDrawCoupon;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.util.RedisWrapperClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

@Service
public class ActivityWeChatDrawCouponService {

    @Autowired
    private MQWrapperClient mqWrapperClient;

    private RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    public boolean duringActivities(ActivityWeChatDrawCoupon activityWeChatDrawCoupon) {
        return ActivityWeChatDrawCoupon.duringActivities(activityWeChatDrawCoupon);
    }

    public boolean drewCoupon(String loginName, ActivityWeChatDrawCoupon activityWeChatDrawCoupon) {
        return redisWrapperClient.exists(MessageFormat.format(activityWeChatDrawCoupon.getKey(), loginName));
    }

    public void sendDrawCouponMessage(String loginName, ActivityWeChatDrawCoupon activityWeChatDrawCoupon) {
        if (duringActivities(activityWeChatDrawCoupon)) {
            mqWrapperClient.sendMessage(MessageQueue.ActivityWeChatCoupon,
                    Maps.newHashMap(ImmutableMap.<String, Object>builder()
                    .put("loginName", loginName)
                    .put("activityWeChatDrawCoupon", activityWeChatDrawCoupon)
                    .build()));
        }
    }
}
