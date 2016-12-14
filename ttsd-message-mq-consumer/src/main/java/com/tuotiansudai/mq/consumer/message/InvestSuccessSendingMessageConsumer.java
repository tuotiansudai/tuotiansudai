package com.tuotiansudai.mq.consumer.message;

import com.tuotiansudai.message.InvestSuccessMessage;
import com.tuotiansudai.message.util.UserMessageEventGenerator;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.util.JsonConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Component
public class InvestSuccessSendingMessageConsumer implements MessageConsumer {
    private static Logger logger = LoggerFactory.getLogger(InvestSuccessSendingMessageConsumer.class);

    @Autowired
    private UserMessageEventGenerator userMessageEventGenerator;

    @Override
    public MessageQueue queue() {
        return MessageQueue.InvestSuccess_SendJpushMessage;
    }

    @Transactional
    @Override
    public void consume(String message) {
        logger.info("[MQ] receive message: {}: '{}'.", this.queue(), message);
        if (!StringUtils.isEmpty(message)) {
            InvestSuccessMessage investSuccessMessage;
            try {
                investSuccessMessage = JsonConverter.readValue(message, InvestSuccessMessage.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            long investId = investSuccessMessage.getInvestInfo().getInvestId();
            logger.info("[MQ] ready to consume message: send jpush message on invest success.");
            userMessageEventGenerator.generateInvestSuccessEvent(investId);
            logger.info("[MQ] consume message success.");
        }
    }
}
