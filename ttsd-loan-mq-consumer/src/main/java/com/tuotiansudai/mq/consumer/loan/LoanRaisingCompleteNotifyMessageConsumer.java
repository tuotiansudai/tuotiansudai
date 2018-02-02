package com.tuotiansudai.mq.consumer.loan;

import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.sms.LoanRaisingCompleteNotifyDto;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.util.JsonConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LoanRaisingCompleteNotifyMessageConsumer implements MessageConsumer {

    private static Logger logger = LoggerFactory.getLogger(LoanRaisingCompleteNotifyMessageConsumer.class);

    @Autowired
    private SmsWrapperClient smsWrapperClient;

    @Override
    public MessageQueue queue() {
        return MessageQueue.LoanRaisingCompleteNotify;
    }

    @Override
    public void consume(String message) {
        logger.info("[MQ] receive message: {}: {}.", this.queue(), message);
        try {
            LoanRaisingCompleteNotifyDto dto = JsonConverter.readValue(message, LoanRaisingCompleteNotifyDto.class);
            smsWrapperClient.sendLoanRaisingCompleteNotify(dto);
            logger.info("[MQ] consume message success.");
        } catch (Exception e) {
            logger.error(String.format("[MQ] send loan raising complete notify sms fail. message: %s", message), e);
        }
    }

}
