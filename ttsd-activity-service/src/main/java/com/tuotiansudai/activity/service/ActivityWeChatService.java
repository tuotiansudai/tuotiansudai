package com.tuotiansudai.activity.service;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.activity.repository.model.ActivityCategory;
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
public class ActivityWeChatService {

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.spring.breeze.startTime}\")}")
    private Date activitySpringBreezeStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.spring.breeze.endTime}\")}")
    private Date activitySpringBreezeEndTime;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    private RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    private static final String SPRING_BREEZE_KEY = "SPRING_BREEZE_ACTIVITY_DRAW_COUPON:{0}";

    public boolean duringActivities(ActivityCategory activityCategory) {
        List<Date> activityTime = getActivityTime(activityCategory);
        return activityTime.get(0).before(new Date()) && activityTime.get(1).after(new Date());
    }

    public boolean drewCoupon(String loginName, ActivityCategory activityCategory) {
        String key = getActivityKey(activityCategory);
        return redisWrapperClient.exists(MessageFormat.format(key, loginName));
    }

    public void sendDrawCouponMessage(String loginName, ActivityCategory activityCategory) {
        if (duringActivities(activityCategory)) {
            mqWrapperClient.sendMessage(MessageQueue.ActivityWeChatCoupon,
                    Maps.newHashMap(ImmutableMap.<String, Object>builder()
                    .put("loginName", loginName)
                    .put("activityCategory", activityCategory)
                    .build()));
        }
    }

    private List<Date> getActivityTime(ActivityCategory activityCategory) {
        return Maps.newHashMap(ImmutableMap.<ActivityCategory, List<Date>>builder()
                .put(ActivityCategory.SPRING_BREEZE_ACTIVITY, Lists.newArrayList(activitySpringBreezeStartTime, activitySpringBreezeEndTime))
                .build()).get(activityCategory);
    }

    private String getActivityKey(ActivityCategory activityCategory) {
        return Maps.newHashMap(ImmutableMap.<ActivityCategory, String>builder()
                .put(ActivityCategory.SPRING_BREEZE_ACTIVITY, SPRING_BREEZE_KEY)
                .build()).get(activityCategory);
    }


}
