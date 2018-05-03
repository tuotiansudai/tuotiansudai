package com.tuotiansudai.activity.service;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.enums.WeChatDrawCoupon;
import com.tuotiansudai.message.WeChatDrawCouponMessage;
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

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.spring.breeze.startTime}\")}")
    private Date activitySpringBreezeStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.spring.breeze.endTime}\")}")
    private Date activitySpringBreezeEndTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.invite.help.startTime}\")}")
    private Date activityInviteHelpStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.invite.help.endTime}\")}")
    private Date activityInviteHelpEndTime;

    private static final String SPRING_BREEZE_KEY = "SPRING_BREEZE_ACTIVITY_DRAW_COUPON:{0}";

    private static final String INVITE_HELP_KEY = "INVITE_HELP_ACTIVITY_DRAW_COUPON:{0}";

    @Autowired
    private MQWrapperClient mqWrapperClient;

    private RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    public boolean duringActivities(WeChatDrawCoupon weChatDrawCoupon) {
        List<Date> activityTime = getActivityTime(weChatDrawCoupon);
        return activityTime.get(0).before(new Date()) && activityTime.get(1).after(new Date());
    }

    public boolean drewCoupon(String loginName, WeChatDrawCoupon weChatDrawCoupon) {
        return redisWrapperClient.exists(MessageFormat.format(getActivityKey(weChatDrawCoupon), loginName));
    }

    public void sendDrawCouponMessage(String loginName, WeChatDrawCoupon weChatDrawCoupon) {
        if (duringActivities(weChatDrawCoupon)) {
            mqWrapperClient.sendMessage(MessageQueue.ActivityWeChatCoupon, new WeChatDrawCouponMessage(loginName, weChatDrawCoupon));
        }
    }

    public List<Date> getActivityTime(WeChatDrawCoupon weChatDrawCoupon){
        return Maps.newHashMap(new ImmutableMap.Builder<WeChatDrawCoupon, List<Date>>()
                .put(WeChatDrawCoupon.SPRING_BREEZE_ACTIVITY_WECHAT, Lists.newArrayList(activitySpringBreezeStartTime, activitySpringBreezeEndTime))
                .put(WeChatDrawCoupon.INVITE_HELP_ACTIVITY_WECHAT, Lists.newArrayList(activityInviteHelpStartTime, activityInviteHelpEndTime))
                .build()).get(weChatDrawCoupon);
    }

    public String getActivityKey(WeChatDrawCoupon weChatDrawCoupon){
        return Maps.newHashMap(new ImmutableMap.Builder<WeChatDrawCoupon, String>()
                .put(WeChatDrawCoupon.SPRING_BREEZE_ACTIVITY_WECHAT, SPRING_BREEZE_KEY)
                .put(WeChatDrawCoupon.INVITE_HELP_ACTIVITY_WECHAT, INVITE_HELP_KEY)
                .build()).get(weChatDrawCoupon);
    }
}
