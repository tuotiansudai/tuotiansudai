package com.tuotiansudai.mq.consumer.activity;

import com.google.common.base.Strings;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.sms.SmsFatalNotifyDto;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
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

//        try {
//            String loginName = message;
//            Map<String, Object> map =
//
//            Date date = new Date();
//            if (date.before(activityStartTime) || date.after(activityEndTime)) {
//                logger.info("[MQ] StartWorkActivity_Coupon, start work activity not in the activity time range");
//                return;
//            }
//            String key = MessageFormat.format(KEY, loginName);
//            if (redisWrapperClient.exists(key)) {
//                logger.info(MessageFormat.format("{0}:已经领取了惊喜不重样加息不打烊活动优惠券", loginName));
//                return;
//            }
//            List<Long> coupons = Arrays.asList(483L, 483L, 484L, 484L, 484L, 484L, 485L, 485L, 486L, 487L);
//            coupons.stream().forEach(couponId -> {
//                try {
//                    logger.info(MessageFormat.format("[StartWorkActivity_Coupon] loginName:{0},couponId:{1} send message begin", loginName, couponId));
//                    mqWrapperClient.sendMessage(MessageQueue.CouponAssigning, loginName + ":" + couponId);
//                    logger.info(MessageFormat.format("[StartWorkActivity_Coupon] loginName:{0},couponId:{1} send message end", loginName, couponId));
//                }catch (Exception e){
//                    logger.error(MessageFormat.format("[StartWorkActivity_Coupon] loginName:{0},couponId:{1} send message fail", loginName, couponId));
//                    smsWrapperClient.sendFatalNotify(new SmsFatalNotifyDto(MessageFormat.format("【惊喜不重样加息不打烊活动】用户:{0}, 优惠券:{1}, 发送优惠券失败, 业务处理异常", loginName, couponId)));
//                }
//            });
//            redisWrapperClient.setex(key, lifeSecond,"SUCCESS");
//
//        } catch (Exception e) {
//            logger.error("[MQ] 程序內部异常: {}: {}.{}", this.queue(), message, e.getMessage());
//        }
    }
}
