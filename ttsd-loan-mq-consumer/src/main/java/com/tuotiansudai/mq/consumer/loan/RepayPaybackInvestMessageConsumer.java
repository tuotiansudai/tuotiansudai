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
public class RepayPaybackInvestMessageConsumer implements MessageConsumer {
    private static Logger logger = LoggerFactory.getLogger(RepayPaybackInvestMessageConsumer.class);

    @Autowired
    private MQWrapperClient mqClient;

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
                boolean flag = true;
                logger.info("[MQ] ready to consume message: repay payback invest.");
                if(String.valueOf(msgParts[1]).equals("0")){
                    //flag = couponRepayService.repay(Long.parseLong(msgParts[0]), false);
                }else{
                    //flag = couponRepayService.repay(Long.parseLong(msgParts[0]), true);
                }
                //UserCouponModel userCoupon = couponAssignmentService.assign(msgParts[0], Long.parseLong(msgParts[1]), null);

                if (flag) {
                    logger.info("[MQ] repay payback invest success.");
                    logger.info("[MQ] consume message success.");
                } else {
                    logger.info("[MQ] repay payback invest fail.");
                    logger.info("[MQ] consume message fail.");
                }
            }
        }
    }
}
