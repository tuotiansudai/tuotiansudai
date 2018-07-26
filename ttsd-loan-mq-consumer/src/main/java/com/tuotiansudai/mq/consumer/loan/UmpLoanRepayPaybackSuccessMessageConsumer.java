package com.tuotiansudai.mq.consumer.loan;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.tuotiansudai.fudian.umpmessage.UmpRepayPaybackMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.service.InvestRepaySuccessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UmpLoanRepayPaybackSuccessMessageConsumer implements MessageConsumer {

    private static Logger logger = LoggerFactory.getLogger(UmpLoanRepayPaybackSuccessMessageConsumer.class);

    private final InvestRepaySuccessService investRepaySuccessService;

    private final Gson gson = new GsonBuilder().create();

    @Autowired
    public UmpLoanRepayPaybackSuccessMessageConsumer(InvestRepaySuccessService investRepaySuccessService) {
        this.investRepaySuccessService = investRepaySuccessService;
    }


    @Override
    public MessageQueue queue() {
        return MessageQueue.UmpRepayPayback_Success;
    }

    @Override
    public void consume(String message) {
        logger.info("[MQ] receive message: {}: {}.", this.queue(), message);

        if (Strings.isNullOrEmpty(message)) {
            logger.warn("[MQ] message is empty");
            return;
        }

        try {
            UmpRepayPaybackMessage umpRepayPaybackMessage = gson.fromJson(message, UmpRepayPaybackMessage.class);
            if (umpRepayPaybackMessage.isNormalRepay()) {
                investRepaySuccessService.processUmpNormalInvestRepaySuccess(umpRepayPaybackMessage);
            } else {
                investRepaySuccessService.processUmpAdvancedInvestRepaySuccess(umpRepayPaybackMessage);
            }
        } catch (JsonSyntaxException e) {
            logger.error("[MQ] message is invalid: {}", message);
        }

        logger.info("[MQ] consume message success.");
    }
}
