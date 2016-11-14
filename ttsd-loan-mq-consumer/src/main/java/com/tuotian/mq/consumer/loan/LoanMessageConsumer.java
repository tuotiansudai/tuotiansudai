package com.tuotian.mq.consumer.loan;

import com.tuotiansudai.mq.client.model.Message;
import com.tuotiansudai.mq.client.model.MessageTopicQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class LoanMessageConsumer implements MessageConsumer {
    private static Logger logger = LoggerFactory.getLogger(LoanMessageConsumer.class);

    @Override
    public MessageTopicQueue queue() {
        return MessageTopicQueue.UserRegistered_CouponAssigning;
    }

    @Transactional
    @Override
    public void consume(Message message) {
        String loginName = message.getMessage();
        logger.info("[MQ] receive message: {}: {}. ignored", this.queue(), loginName);
    }
}
