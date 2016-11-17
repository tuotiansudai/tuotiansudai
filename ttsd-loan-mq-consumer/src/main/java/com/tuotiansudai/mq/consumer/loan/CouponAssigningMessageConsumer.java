package com.tuotiansudai.mq.consumer.loan;

import com.tuotiansudai.coupon.service.CouponAssignmentService;
import com.tuotiansudai.mq.client.model.Message;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.client.model.Queue;
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
    private CouponAssignmentService couponAssignmentService;

    @Override
    public Queue queue() {
        return MessageQueue.CouponAssigning;
    }

    @Transactional
    @Override
    public void consume(Message message) {
        String msg = message.getMessage();
        logger.info("[MQ] receive message[{}]: {}: {}.", message.getMessageId(), this.queue(), msg);
        if (!StringUtils.isEmpty(msg)) {
            String[] msgParts = msg.split(":");
            if (msgParts.length == 2) {
                logger.info("[MQ] ready to consumer message[{}]: assigning coupon.", message.getMessageId());
                couponAssignmentService.assign(msgParts[0], Long.parseLong(msgParts[1]), null);
                logger.info("[MQ] consumer message[{}] fail: {}: {}.", message.getMessageId());
            }
        }
    }
}
