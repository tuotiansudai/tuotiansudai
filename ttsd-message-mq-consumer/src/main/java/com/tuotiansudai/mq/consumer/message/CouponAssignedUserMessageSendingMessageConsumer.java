package com.tuotiansudai.mq.consumer.message;

import com.tuotiansudai.message.util.UserMessageEventGenerator;
import com.tuotiansudai.mq.client.model.Message;
import com.tuotiansudai.mq.client.model.MessageTopicQueue;
import com.tuotiansudai.mq.client.model.Queue;
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
    public Queue queue() {
        return MessageTopicQueue.CouponAssigned_UserMessageSending;
    }

    @Transactional
    @Override
    public void consume(Message message) {
        String msg = message.getMessage();
        logger.info("[MQ] receive message[{}]: {}: {}.", message.getMessageId(), this.queue(), msg);
        if (!StringUtils.isEmpty(msg)) {
            String[] msgParts = msg.split(":");
            if (msgParts.length == 2) {
                long userCouponId = Long.parseLong(msgParts[1]);
                logger.info("[MQ] ready to consumer message[{}]: send user message on assigning coupon.", message.getMessageId());
                userMessageEventGenerator.generateAssignCouponSuccessEvent(userCouponId);
                logger.info("[MQ] consumer message[{}] success.", message.getMessageId());
            }
        }
    }
}
