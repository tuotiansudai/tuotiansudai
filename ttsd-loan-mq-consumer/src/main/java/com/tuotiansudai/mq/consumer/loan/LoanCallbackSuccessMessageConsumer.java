package com.tuotiansudai.mq.consumer.loan;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.tuotiansudai.fudian.message.BankLoanCallbackMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.service.InvestRepaySuccessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LoanCallbackSuccessMessageConsumer implements MessageConsumer {

    private static Logger logger = LoggerFactory.getLogger(LoanCallbackSuccessMessageConsumer.class);

    private final InvestRepaySuccessService investRepaySuccessService;

    private final Gson gson = new GsonBuilder().create();

    @Autowired
    public LoanCallbackSuccessMessageConsumer(InvestRepaySuccessService investRepaySuccessService) {
        this.investRepaySuccessService = investRepaySuccessService;
    }


    @Override
    public MessageQueue queue() {
        return MessageQueue.LoanCallback_Success;
    }

    @Override
    public void consume(String message) {
        logger.info("[MQ] receive message: {}: {}.", this.queue(), message);

        if (Strings.isNullOrEmpty(message)) {
            logger.warn("[MQ] message is empty");
            return;
        }

        try {
            BankLoanCallbackMessage bankLoanCallbackMessage = gson.fromJson(message, BankLoanCallbackMessage.class);
            if (bankLoanCallbackMessage.isNormalRepay()) {
                investRepaySuccessService.processNormalInvestRepaySuccess(bankLoanCallbackMessage);
            } else {
                investRepaySuccessService.processAdvancedInvestRepaySuccess(bankLoanCallbackMessage);
            }
        } catch (JsonSyntaxException e) {
            logger.error("[MQ] message is invalid: {}", message);
        }

        logger.info("[MQ] consume message success.");
    }
}
