package com.tuotiansudai.mq.consumer.loan;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.tuotiansudai.fudian.umpmessage.UmpLoanRepayMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.service.LoanRepaySuccessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UmpLoanRepaySuccessMessageConsumer implements MessageConsumer {

    private static Logger logger = LoggerFactory.getLogger(UmpLoanRepaySuccessMessageConsumer.class);

    private final LoanRepaySuccessService loanRepaySuccessService;

    private final Gson gson = new GsonBuilder().create();

    @Autowired
    public UmpLoanRepaySuccessMessageConsumer(LoanRepaySuccessService loanRepaySuccessService) {
        this.loanRepaySuccessService = loanRepaySuccessService;
    }


    @Override
    public MessageQueue queue() {
        return MessageQueue.UmpLoanRepay_Success;
    }

    @Override
    public void consume(String message) {
        logger.info("[MQ] receive message: {}: {}.", this.queue(), message);

        if (Strings.isNullOrEmpty(message)) {
            logger.warn("[MQ] message is empty");
            return;
        }

        try {
            UmpLoanRepayMessage umpLoanRepayMessage = gson.fromJson(message, UmpLoanRepayMessage.class);
            if (umpLoanRepayMessage.isNormalRepay()) {
                loanRepaySuccessService.processUmpNormalLoanRepaySuccess(umpLoanRepayMessage);
            } else {
                loanRepaySuccessService.processUmpAdvancedLoanRepaySuccess(umpLoanRepayMessage);
            }
        } catch (JsonSyntaxException e) {
            logger.error("[MQ] message is invalid: {}", message);
        }

        logger.info("[MQ] consume message success.");
    }
}
