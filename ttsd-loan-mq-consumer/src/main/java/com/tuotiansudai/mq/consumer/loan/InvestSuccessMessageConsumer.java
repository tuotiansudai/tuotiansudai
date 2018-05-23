package com.tuotiansudai.mq.consumer.loan;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.tuotiansudai.fudian.message.BankInvestMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.InvestStatus;
import com.tuotiansudai.service.InvestSuccessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class InvestSuccessMessageConsumer implements MessageConsumer {
    private static Logger logger = LoggerFactory.getLogger(InvestSuccessMessageConsumer.class);

    private final InvestMapper investMapper;

    private final InvestSuccessService investSuccessService;

    private final Gson gson  = new GsonBuilder().create();

    @Autowired
    public InvestSuccessMessageConsumer(InvestMapper investMapper, InvestSuccessService investSuccessService) {
        this.investMapper = investMapper;
        this.investSuccessService = investSuccessService;
    }

    @Override
    public MessageQueue queue() {
        return MessageQueue.Invest_Success;
    }

    @Override
    public void consume(String message) {
        logger.info("[MQ] receive message: {}: {}.", this.queue(), message);

        if (Strings.isNullOrEmpty(message)) {
            logger.warn("[MQ] message is empty");
            return;
        }

        try {
            BankInvestMessage bankInvestMessage = gson.fromJson(message, BankInvestMessage.class);
            investSuccessService.processInvestSuccess(bankInvestMessage);
        } catch (JsonSyntaxException e) {
            logger.error("[MQ] message is invalid: {}", message);
        }

        logger.info("[MQ] consume message success.");
    }
}
