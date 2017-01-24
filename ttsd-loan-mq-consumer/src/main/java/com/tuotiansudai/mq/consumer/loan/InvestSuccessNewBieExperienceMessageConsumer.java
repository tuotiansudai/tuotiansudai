package com.tuotiansudai.mq.consumer.loan;

import com.tuotiansudai.message.InvestSuccessMessage;
import com.tuotiansudai.message.UserInfo;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.util.JsonConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Component
public class InvestSuccessNewBieExperienceMessageConsumer implements MessageConsumer {
    private static Logger logger = LoggerFactory.getLogger(InvestSuccessNewBieExperienceMessageConsumer.class);

    @Override
    public MessageQueue queue() {
        return MessageQueue.InvestSuccess_ActivityReward;
    }

    @Transactional
    @Override
    public void consume(String message) {
        logger.info("[MQ] receive message: {}: {}.", this.queue(), message);
        InvestSuccessMessage investSuccessMessage = null;
        try {
            investSuccessMessage = JsonConverter.readValue(message, InvestSuccessMessage.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        

    }

    private boolean isSendExperienceInterest(UserInfo userInfo){

    }



}
