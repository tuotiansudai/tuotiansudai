package com.tuotiansudai.mq.consumer.loan;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.tuotiansudai.fudian.umpmessage.UmpExtraRepayMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.service.InvestRepaySuccessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UmpExtraRepaySuccessMessageConsumer implements MessageConsumer {

    private static Logger logger = LoggerFactory.getLogger(UmpExtraRepaySuccessMessageConsumer.class);

    private final InvestRepaySuccessService investRepaySuccessService;

    private final Gson gson = new GsonBuilder().create();

    @Autowired
    public UmpExtraRepaySuccessMessageConsumer(InvestRepaySuccessService investRepaySuccessService) {
        this.investRepaySuccessService = investRepaySuccessService;
    }


    @Override
    public MessageQueue queue() {
        return MessageQueue.UmpExtraRepay_Success;
    }

    @Override
    public void consume(String message) {
        logger.info("[MQ] receive message: {}: {}.", this.queue(), message);

        if (Strings.isNullOrEmpty(message)) {
            logger.warn("[MQ] message is empty");
            return;
        }

        try {
            UmpExtraRepayMessage umpExtraRepayMessage = gson.fromJson(message, UmpExtraRepayMessage.class);
            investRepaySuccessService.processExtraRepaySuccess(umpExtraRepayMessage);
        } catch (JsonSyntaxException e) {
            logger.error("[MQ] message is invalid: {}", message);
        }

        logger.info("[MQ] consume message success.");
    }
}
