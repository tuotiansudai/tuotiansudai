package com.tuotiansudai.mq.consumer.loan;

import com.google.common.collect.Lists;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.dto.sms.JianZhouSmsTemplate;
import com.tuotiansudai.dto.sms.SmsDto;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CreditLoanBalanceAlertMessageConsumer implements MessageConsumer {

    private static Logger logger = LoggerFactory.getLogger(CreditLoanBalanceAlertMessageConsumer.class);

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Value("${credit.loan.agent}")
    private String creditLoanAgent;

    @Override
    public MessageQueue queue() {
        return MessageQueue.CreditLoanBalanceAlert;
    }

    @Override
    public void consume(String message) {
        logger.info("[MQ] receive message: {}: {}.", this.queue(), message);
        mqWrapperClient.sendMessage(MessageQueue.UserSms, new SmsDto(JianZhouSmsTemplate.SMS_CREDIT_LOAN_BALANCE_ALERT_TEMPLATE, Lists.newArrayList(creditLoanAgent)));
        logger.info("[MQ] consume message success.");
    }

}
