package com.tuotiansudai.mq.consumer.activity;

import com.google.common.base.Strings;
import com.tuotiansudai.activity.repository.model.ActivityWeChatDrawCoupon;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.sms.SmsFatalNotifyDto;
import com.tuotiansudai.message.InvestSuccessMessage;
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
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class ActivityWeChatDrawCouponMessageConsumer implements MessageConsumer {
    private static Logger logger = LoggerFactory.getLogger(ActivityWeChatDrawCouponMessageConsumer.class);

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.spring.breeze.startTime}\")}")
    private Date activitySpringBreezeStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.spring.breeze.endTime}\")}")
    private Date activitySpringBreezeEndTime;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Autowired
    private SmsWrapperClient smsWrapperClient;

    private RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    private static final String SPRING_BREEZE_KEY = "SPRING_BREEZE_ACTIVITY_DRAW_COUPON:{0}";

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
            Map map = JsonConverter.readValue(message, Map.class);
            String loginName = (String) map.get("loginName");
            ActivityWeChatDrawCoupon activityWeChatDrawCoupon = (ActivityWeChatDrawCoupon) map.get("activityWeChatDrawCoupon");

            if (ActivityWeChatDrawCoupon.duringActivities(activityWeChatDrawCoupon)){
                String key = MessageFormat.format(activityWeChatDrawCoupon.getKey(), loginName);

                if (redisWrapperClient.exists(key)) {
                    logger.info(MessageFormat.format("{0},{1} already draw coupon", activityWeChatDrawCoupon.name(), loginName));
                    return;
                }
                List<Long> coupons = activityWeChatDrawCoupon.getCoupons();
                coupons.stream().forEach(couponId -> {
                    try {
                        logger.info(MessageFormat.format("[{0}] loginName:{1},couponId:{2} send message begin", activityWeChatDrawCoupon.name(), loginName, couponId));
                        mqWrapperClient.sendMessage(MessageQueue.CouponAssigning, loginName + ":" + couponId);
                        logger.info(MessageFormat.format("[{0}] loginName:{1},couponId:{2} send message end", activityWeChatDrawCoupon.name(), loginName, couponId));
                    }catch (Exception e){
                        logger.error(MessageFormat.format("[{0}] loginName:{1},couponId:{2} send message fail", activityWeChatDrawCoupon.name(), loginName, couponId));
                        smsWrapperClient.sendFatalNotify(new SmsFatalNotifyDto(MessageFormat.format("【{0}】用户:{1}, 优惠券:{2}, 发送优惠券失败, 业务处理异常", activityWeChatDrawCoupon.name(), loginName, couponId)));
                    }
                });
                redisWrapperClient.setex(key, lifeSecond,"SUCCESS");

            }else {
                logger.info(MessageFormat.format("[MQ] ActivityWeChatCoupon, {0} not in the activity time range", activityWeChatDrawCoupon.name()));
            }

        } catch (Exception e) {
            logger.error("[MQ] 程序內部异常: {}: {}.{}", this.queue(), message, e.getMessage());
        }
    }
}