package com.tuotiansudai.mq.consumer.loan;

import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.coupon.service.CouponAssignmentService;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Component
public class CouponAssigningMessageConsumer implements MessageConsumer {
    private static Logger logger = LoggerFactory.getLogger(CouponAssigningMessageConsumer.class);

    @Autowired
    private MQWrapperClient mqClient;

    @Autowired
    private CouponAssignmentService couponAssignmentService;

    @Override
    public MessageQueue queue() {
        return MessageQueue.CouponAssigning;
    }

    @Transactional
    @Override
    public void consume(String message) {
        logger.info("[MQ] receive message: {}: {}.", this.queue(), message);
        if (!StringUtils.isEmpty(message)) {
            String[] msgParts = message.split(":");
            if (msgParts.length == 2) {
                logger.info("[MQ] ready to consume message: assigning coupon.");
                UserCouponModel userCoupon = couponAssignmentService.assign(msgParts[0], Long.parseLong(msgParts[1]), null);
                if (userCoupon != null) {
                    logger.info("[MQ] assigning coupon success, begin publish message.");
                    mqClient.sendMessage(MessageQueue.CouponAssigned_UserMessageSending, "UserCoupon:" + userCoupon.getId());
                } else {
                    logger.info("[MQ] no user coupon assign.");
                }
                logger.info("[MQ] consume message success.");
            }
        }
    }
}
