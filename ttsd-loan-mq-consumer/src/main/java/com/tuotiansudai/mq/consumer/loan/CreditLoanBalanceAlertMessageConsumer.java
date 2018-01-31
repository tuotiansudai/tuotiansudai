package com.tuotiansudai.mq.consumer.loan;

import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CreditLoanBalanceAlertMessageConsumer implements MessageConsumer {

    private static Logger logger = LoggerFactory.getLogger(CreditLoanBalanceAlertMessageConsumer.class);

    @Autowired
    private SmsWrapperClient smsWrapperClient;

    @Override
    public MessageQueue queue() {
        return MessageQueue.CreditLoanBalanceAlert;
    }

    @Override
    public void consume(String message) {
        logger.info("[MQ] receive message: {}: {}.", this.queue(), message);
        smsWrapperClient.sendCreditLoanBalanceAlert();
        logger.info("[MQ] consume message success.");
    }

}
