package com.tuotiansudai.mq.consumer.loan;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.tuotiansudai.fudian.message.BankLoanCreditInvestMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.service.LoanCreditInvestSuccessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LoanCreditInvestSuccessMessageConsumer implements MessageConsumer{

    private static Logger logger = LoggerFactory.getLogger(LoanCreditInvestSuccessMessageConsumer.class);

    private final LoanCreditInvestSuccessService loanCreditInvestSuccessService;

    private final Gson gson  = new GsonBuilder().create();

    @Autowired
    public LoanCreditInvestSuccessMessageConsumer(LoanCreditInvestSuccessService loanCreditInvestSuccessService) {
        this.loanCreditInvestSuccessService = loanCreditInvestSuccessService;
    }

    @Override
    public MessageQueue queue() {
        return MessageQueue.LoanCreditInvest_Success;
    }

    @Override
    public void consume(String message) {
        logger.info("[MQ] receive message: {}: {}.", this.queue(), message);

        if (Strings.isNullOrEmpty(message)) {
            logger.warn("[MQ] message is empty");
            return;
        }

        try {
            BankLoanCreditInvestMessage bankLoanCreditInvestMessage = gson.fromJson(message, BankLoanCreditInvestMessage.class);
            loanCreditInvestSuccessService.processLoanCreditInvestSuccess(bankLoanCreditInvestMessage);
        } catch (JsonSyntaxException e) {
            logger.error("[MQ] message is invalid: {}", message);
        }

        logger.info("[MQ] consume message success.");

    }
}
