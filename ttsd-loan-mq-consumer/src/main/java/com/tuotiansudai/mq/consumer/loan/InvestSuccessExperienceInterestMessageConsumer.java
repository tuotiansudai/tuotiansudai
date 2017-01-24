package com.tuotiansudai.mq.consumer.loan;

import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Component
public class InvestSuccessExperienceInterestMessageConsumer implements MessageConsumer {
    private static Logger logger = LoggerFactory.getLogger(InvestSuccessExperienceInterestMessageConsumer.class);

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Override
    public MessageQueue queue() {
        return MessageQueue.InvestSuccess_NewBieExperience_Interest;
    }

    @Transactional
    @Override
    public void consume(String message) {
        logger.info("[MQ] receive message: {}: {}.", this.queue(), message);
        if (!StringUtils.isEmpty(message)) {
            String[] msgParts = message.split(":");
            if (msgParts.length == 2) {
                logger.info("[MQ] ready to consume message: send experience interest.");

            }
        }
    }
}
