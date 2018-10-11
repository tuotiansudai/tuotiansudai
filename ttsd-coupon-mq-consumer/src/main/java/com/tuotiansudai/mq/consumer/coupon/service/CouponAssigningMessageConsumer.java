package com.tuotiansudai.mq.consumer.coupon.service;

import com.tuotiansudai.coupon.service.CouponAssignmentService;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CouponAssigningMessageConsumer implements MessageConsumer {
    private static Logger logger = LoggerFactory.getLogger(CouponAssigningMessageConsumer.class);

    @Autowired
    private CouponAssignmentService couponAssignmentService;

    @Override
    public MessageQueue queue() {
        return MessageQueue.Coupon_Assigning;
    }

    @Override
    public void consume(String message) {
        logger.info("[MQ] receive message: {}: {}.", this.queue(), message);
        if (!StringUtils.isEmpty(message)) {
            String[] msgParts = message.split(":");
            if (msgParts.length == 2) {
                logger.info("[MQ] ready to consume message: assigning coupon.");
                couponAssignmentService.assign(msgParts[0], Long.parseLong(msgParts[1]), null);
            }if(msgParts.length == 3){
                logger.info("[MQ] ready to consume message: assigning achievement coupon.");
                couponAssignmentService.assignInvestAchievementUserCoupon(msgParts[0], Long.parseLong(msgParts[1]), Long.parseLong(msgParts[2]));
            }
        }
    }
}
