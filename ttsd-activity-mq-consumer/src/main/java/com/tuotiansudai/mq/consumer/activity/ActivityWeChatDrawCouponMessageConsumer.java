package com.tuotiansudai.mq.consumer.activity;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.enums.WeChatDrawCoupon;
import com.tuotiansudai.message.WeChatDrawCouponMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.util.JsonConverter;
import com.tuotiansudai.util.RedisWrapperClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

@Component
public class ActivityWeChatDrawCouponMessageConsumer implements MessageConsumer {
    private static Logger logger = LoggerFactory.getLogger(ActivityWeChatDrawCouponMessageConsumer.class);

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.spring.breeze.startTime}\")}")
    private Date activitySpringBreezeStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.spring.breeze.endTime}\")}")
    private Date activitySpringBreezeEndTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.invite.help.startTime}\")}")
    private Date activityInviteHelpStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.invite.help.endTime}\")}")
    private Date activityInviteHelpEndTime;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    private RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    private static final String SPRING_BREEZE_KEY = "SPRING_BREEZE_ACTIVITY_DRAW_COUPON:{0}";

    private static final String INVITE_HELP_KEY = "INVITE_HELP_ACTIVITY_DRAW_COUPON:{0}";

    private static final int lifeSecond = 60 * 60 * 24 * 180;

    @Override
    public MessageQueue queue() {
        return MessageQueue.ActivityWeChatCoupon;
    }

    @Override
    public void consume(String message) {
        logger.info("[MQ] receive message: {}: {}.", this.queue(), message);

        if (Strings.isNullOrEmpty(message)) {
            logger.error("[MQ] message is empty");
            return;
        }

        try {
            WeChatDrawCouponMessage weChatDrawCouponMessage = JsonConverter.readValue(message, WeChatDrawCouponMessage.class);
            String loginName = weChatDrawCouponMessage.getLoginName();
            WeChatDrawCoupon weChatDrawCoupon = weChatDrawCouponMessage.getWeChatDrawCoupon();

            List<Date> activityTime = getActivityTime(weChatDrawCoupon);

            if (activityTime.get(0).before(new Date()) && activityTime.get(1).after(new Date())){
                String key = MessageFormat.format(getActivityKey(weChatDrawCoupon), loginName);

                if (redisWrapperClient.exists(key)) {
                    logger.info(MessageFormat.format("{0},{1} already draw coupon", weChatDrawCoupon.name(), loginName));
                    return;
                }
                List<Long> coupons = getActivityCoupons(weChatDrawCoupon);
                coupons.stream().forEach(couponId -> {
                    try {
                        logger.info(MessageFormat.format("[{0}] loginName:{1},couponId:{2} send message begin", weChatDrawCoupon.name(), loginName, couponId));
                        mqWrapperClient.sendMessage(MessageQueue.CouponAssigning, loginName + ":" + couponId);
                        logger.info(MessageFormat.format("[{0}] loginName:{1},couponId:{2} send message end", weChatDrawCoupon.name(), loginName, couponId));
                    }catch (Exception e){
                        logger.error(MessageFormat.format("[{0}] loginName:{1},couponId:{2} send message fail", weChatDrawCoupon.name(), loginName, couponId));
                        mqWrapperClient.sendMessage(MessageQueue.SmsFatalNotify, MessageFormat.format("【{0}】用户:{1}, 优惠券:{2}, 发送优惠券失败, 业务处理异常", weChatDrawCoupon.name(), loginName, couponId));
                    }
                });
                redisWrapperClient.setex(key, lifeSecond,"SUCCESS");

            }else {
                logger.info(MessageFormat.format("[MQ] ActivityWeChatCoupon, {0} not in the activity time range", weChatDrawCoupon.name()));
            }

        } catch (Exception e) {
            logger.error("[MQ] 程序内部异常: {}: {}.{}", this.queue(), message, e.getMessage());
        }
    }

    private List<Date> getActivityTime(WeChatDrawCoupon weChatDrawCoupon) {
        return Maps.newHashMap(new ImmutableMap.Builder<WeChatDrawCoupon, List<Date>>()
                .put(WeChatDrawCoupon.SPRING_BREEZE_ACTIVITY_WECHAT, Lists.newArrayList(activitySpringBreezeStartTime, activitySpringBreezeEndTime))
                .put(WeChatDrawCoupon.INVITE_HELP_ACTIVITY_WECHAT, Lists.newArrayList(activityInviteHelpStartTime, activityInviteHelpEndTime))
                .build()).get(weChatDrawCoupon);
    }

    private String getActivityKey(WeChatDrawCoupon weChatDrawCoupon) {
        return Maps.newHashMap(new ImmutableMap.Builder<WeChatDrawCoupon, String>()
                .put(WeChatDrawCoupon.SPRING_BREEZE_ACTIVITY_WECHAT, SPRING_BREEZE_KEY)
                .put(WeChatDrawCoupon.INVITE_HELP_ACTIVITY_WECHAT, INVITE_HELP_KEY)
                .build()).get(weChatDrawCoupon);
    }

    private List<Long> getActivityCoupons(WeChatDrawCoupon weChatDrawCoupon){
        return Maps.newHashMap(new ImmutableMap.Builder<WeChatDrawCoupon, List<Long>>()
                .put(WeChatDrawCoupon.SPRING_BREEZE_ACTIVITY_WECHAT, Lists.newArrayList(488L, 489L, 489L, 489L, 490L, 490L, 490L, 490L, 490L, 491L, 492L, 493L))
                .put(WeChatDrawCoupon.INVITE_HELP_ACTIVITY_WECHAT, Lists.newArrayList(494L, 494L))
                .build()).get(weChatDrawCoupon);
    }
}
