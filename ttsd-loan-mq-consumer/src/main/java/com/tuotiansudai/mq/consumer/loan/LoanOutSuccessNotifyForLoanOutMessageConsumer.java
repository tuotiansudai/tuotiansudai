package com.tuotiansudai.mq.consumer.loan;

import com.google.common.base.Strings;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.sms.SmsFatalNotifyDto;
import com.tuotiansudai.message.LoanOutSuccessMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.util.JsonConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.MessageFormat;

@Component
public class LoanOutSuccessNotifyForLoanOutMessageConsumer implements MessageConsumer {
    private static Logger logger = LoggerFactory.getLogger(LoanOutSuccessNotifyForLoanOutMessageConsumer.class);

    @Autowired
    private SmsWrapperClient smsWrapperClient;

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Override
    public MessageQueue queue() {
        return MessageQueue.LoanOutSuccess_SmsMessage;
    }

    @Override
    public void consume(String message) {
        logger.info("[标的放款MQ] LoanOutSuccess_SmsMessage receive message: {}: {}.", this.queue(), message);
        if (Strings.isNullOrEmpty(message)) {
            logger.error("[标的放款MQ] LoanOutSuccess_SmsMessage receive message is empty");
            smsWrapperClient.sendFatalNotify(new SmsFatalNotifyDto("放款发送短信, MQ消息为空"));
            return;
        }

        LoanOutSuccessMessage loanOutInfo;
        try {
            loanOutInfo = JsonConverter.readValue(message, LoanOutSuccessMessage.class);
            if (loanOutInfo.getLoanId() == null) {
                logger.error("[标的放款MQ] LoanOutSuccess_SmsMessage loanId is empty");
                smsWrapperClient.sendFatalNotify(new SmsFatalNotifyDto("放款发送短信, 消息中loanId为空"));
                return;
            }
        } catch (IOException e) {
            logger.error("[标的放款MQ] LoanOutSuccess_SmsMessage json convert LoanOutSuccessMessage is fail, message:{}", message);
            return;
        }

        long loanId = loanOutInfo.getLoanId();

        logger.info("[标的放款MQ] LoanOutSuccess_SmsMessage is executing，loanId:" + loanId);
        if (!payWrapperClient.processNotifyForLoanOut(loanId).isSuccess()) {
            smsWrapperClient.sendFatalNotify(new SmsFatalNotifyDto(MessageFormat.format("发送放款短信通知失败,标的ID:{0}", String.valueOf(loanId))));
            logger.error(MessageFormat.format("[标的放款MQ] LoanOutSuccess_SmsMessage is fail (loanId = {0})", String.valueOf(loanId)));
        }

        logger.info("[标的放款MQ] LoanOutSuccess_SmsMessage consume success.");
    }
}
