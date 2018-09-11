package com.tuotiansudai.mq.consumer.loan;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.tuotiansudai.fudian.message.BankLoanCancelMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.service.LoanCancelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class LoanCancelSuccessMessageConsumer implements MessageConsumer {

    private static Logger logger = LoggerFactory.getLogger(LoanCancelSuccessMessageConsumer.class);

    private static Gson gson = new GsonBuilder().create();

    private final LoanCancelService loanCancelService;

    @Autowired
    public LoanCancelSuccessMessageConsumer(LoanCancelService loanCancelService) {
        this.loanCancelService = loanCancelService;
    }

    @Override
    public MessageQueue queue() {
        return MessageQueue.LoanCancel_Success;
    }

    @Override
    public void consume(String message) {
        logger.info("[MQ] receive message: {}: {}.", this.queue(), message);

        if (Strings.isNullOrEmpty(message)) {
            logger.warn("[MQ] message is empty");
            return;
        }

        try {
            BankLoanCancelMessage bankLoanCancelMessage = gson.fromJson(message, BankLoanCancelMessage.class);
            loanCancelService.cancel(bankLoanCancelMessage);
        } catch (JsonSyntaxException e) {
            logger.error(MessageFormat.format("[MQ] parse message failure, message: {0}", message), e);
        }

    }
}
