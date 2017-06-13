package com.tuotiansudai.mq.consumer.activity;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.activity.repository.mapper.CelebrationDrawCouponMapper;
import com.tuotiansudai.activity.repository.mapper.MidSummerInvestMapper;
import com.tuotiansudai.activity.repository.model.CelebrationDrawCouponModel;
import com.tuotiansudai.activity.repository.model.MidSummerInvestModel;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.message.InvestSuccessMidSummerMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.util.JsonConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class CelebrationDrawCouponMessageConsumer implements MessageConsumer {
    private static Logger logger = LoggerFactory.getLogger(CelebrationDrawCouponMessageConsumer.class);
    @Autowired
    public CelebrationDrawCouponMapper celebrationDrawCouponMapper;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.celebration.drawCoupon.startTime}\")}")
    private Date activityStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.celebration.drawCoupon.endTime}\")}")
    private Date activityEndTime;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    private static Map<String, Long> coupons = Maps.newHashMap(ImmutableMap.<String, Long>builder()
            .put("RED_ENVELOPE_OF_10_COUPON_ID", 430L)
            .put("RED_ENVELOPE_OF_20_COUPON_ID", 431L)
            .put("RED_ENVELOPE_OF_30_COUPON_ID", 432L)
            .put("RED_ENVELOPE_OF_60_COUPON_ID", 433L)
            .put("RED_ENVELOPE_OF_100_COUPON_ID", 434L)
            .put("RED_ENVELOPE_OF_180_COUPON_ID", 435L)
            .put("RED_ENVELOPE_OF_200_COUPON_ID", 436L)
            .put("RED_ENVELOPE_OF_400_COUPON_ID", 437L)
            .put("INTEREST_COUPON_OF_ZERO_8_PERCENT_COUPON_ID", 438L)
            .build());

    @Override
    public MessageQueue queue() {
        return MessageQueue.Celebration_Coupon;
    }


    @Override
    public void consume(String message) {
        logger.info("[MQ] receive message: {}: {}.", this.queue(), message);

        if (Strings.isNullOrEmpty(message)) {
            logger.error("[MQ] message is empty");
            return;
        }

        try {
            String loginName = message;
            if (!duringActivities()) {
                logger.info("周年庆领取优惠券活动已经结束!");
                return;
            }
            if (celebrationDrawCouponMapper.findByLoginName(loginName) != null) {
                logger.info(MessageFormat.format("{0} 已经领取了周年庆优惠券", loginName));
                return;
            }
            celebrationDrawCouponMapper.create(new CelebrationDrawCouponModel(loginName));
            coupons.values().stream().forEach(couponId -> {
                logger.info(MessageFormat.format("loginName:{0},couponId:{1} send message begin", loginName, couponId));
                mqWrapperClient.sendMessage(MessageQueue.CouponAssigning, loginName + ":" + couponId);
                logger.info(MessageFormat.format("loginName:{0},couponId:{1} send message end", loginName, couponId));
            });

        } catch (Exception e) {
            logger.error("[MQ] 程序內部异常: {}: {}.{}", this.queue(), message, e.getMessage());
        }
    }

    private boolean duringActivities() {
        return activityStartTime.before(new Date()) && activityEndTime.after(new Date());
    }
}
