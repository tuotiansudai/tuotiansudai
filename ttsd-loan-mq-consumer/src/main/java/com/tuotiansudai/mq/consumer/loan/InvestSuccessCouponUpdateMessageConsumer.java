package com.tuotiansudai.mq.consumer.loan;

import com.tuotiansudai.coupon.service.UserCouponService;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Component
public class InvestSuccessCouponUpdateMessageConsumer implements MessageConsumer {
    private static Logger logger = LoggerFactory.getLogger(InvestSuccessCouponUpdateMessageConsumer.class);

    @Autowired
    private UserCouponService userCouponService;

    @Override
    public MessageQueue queue() {
        return MessageQueue.InvestSuccess_CouponUpdate;
    }

    @Transactional
    @Override
    public void consume(String message) {
        logger.info("[MQ] receive message: {}: {}.", this.queue(), message);
        if (!StringUtils.isEmpty(message)) {
            logger.info("[MQ] ready to consume message: Invest Success Coupon Update. investId:{}", message);
            userCouponService.updateCouponAndAssign(Long.parseLong(message));
            logger.info("[MQ] consume message success.");
        }
    }
}
