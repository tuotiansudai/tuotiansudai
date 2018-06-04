package com.tuotiansudai.mq.consumer.loan;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.tuotiansudai.fudian.message.BankLoanRepayMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.service.LoanRepaySuccessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LoanRepaySuccessMessageConsumer implements MessageConsumer {

    private static Logger logger = LoggerFactory.getLogger(LoanRepaySuccessMessageConsumer.class);

    private final LoanRepaySuccessService loanRepaySuccessService;

    private final Gson gson = new GsonBuilder().create();

    @Autowired
    public LoanRepaySuccessMessageConsumer(LoanRepaySuccessService loanRepaySuccessService) {
        this.loanRepaySuccessService = loanRepaySuccessService;
    }


    @Override
    public MessageQueue queue() {
        return MessageQueue.LoanRepay_Success;
    }

    @Override
    public void consume(String message) {
        logger.info("[MQ] receive message: {}: {}.", this.queue(), message);

        if (Strings.isNullOrEmpty(message)) {
            logger.warn("[MQ] message is empty");
            return;
        }

        try {
            BankLoanRepayMessage bankLoanRepayMessage = gson.fromJson(message, BankLoanRepayMessage.class);
            if (bankLoanRepayMessage.isNormalRepay()) {
                loanRepaySuccessService.processNormalLoanRepaySuccess(bankLoanRepayMessage);
            } else {
                loanRepaySuccessService.processAdvancedLoanRepaySuccess(bankLoanRepayMessage);
            }
        } catch (JsonSyntaxException e) {
            logger.error("[MQ] message is invalid: {}", message);
        }

        logger.info("[MQ] consume message success.");
    }
}
