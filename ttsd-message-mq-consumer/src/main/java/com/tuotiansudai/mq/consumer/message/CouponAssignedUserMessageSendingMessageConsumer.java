package com.tuotiansudai.mq.consumer.message;

import com.tuotiansudai.message.util.UserMessageEventGenerator;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Component
public class CouponAssignedUserMessageSendingMessageConsumer implements MessageConsumer {
    private static Logger logger = LoggerFactory.getLogger(CouponAssignedUserMessageSendingMessageConsumer.class);

    @Autowired
    private UserMessageEventGenerator userMessageEventGenerator;

    @Override
    public MessageQueue queue() {
        return MessageQueue.CouponAssigned_UserMessageSending;
    }

    @Transactional
    @Override
    public void consume(String message) {
        logger.info("[MQ] receive message: {}: '{}'.", this.queue(), message);
        if (!StringUtils.isEmpty(message)) {
            String[] msgParts = message.split(":");
            if (msgParts.length == 2) {
                long userCouponId = Long.parseLong(msgParts[1]);
                logger.info("[MQ] ready to consumer message: send user message on assigning coupon.");
                userMessageEventGenerator.generateAssignCouponSuccessEvent(userCouponId);
                logger.info("[MQ] consumer message success.");
            }
        }
    }
}
