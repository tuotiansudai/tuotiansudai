package com.tuotiansudai.mq.consumer.user;


import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.tuotiansudai.fudian.message.BankWithdrawMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.service.BankWithdrawService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class BankWithdrawMessageConsumer implements MessageConsumer {

    private static Logger logger = LoggerFactory.getLogger(BankWithdrawMessageConsumer.class);

    private final BankWithdrawService bankWithdrawService;

    @Autowired
    public BankWithdrawMessageConsumer(BankWithdrawService bankWithdrawService) {
        this.bankWithdrawService = bankWithdrawService;
    }

    @Override
    public MessageQueue queue() {
        return MessageQueue.Withdraw_Success;
    }

    @Override
    public void consume(String message) {
        logger.info("[MQ] receive message: {}: {}.", this.queue(), message);

        if (Strings.isNullOrEmpty(message)) {
            logger.error("[MQ] Withdraw_Success message is empty");
            return;
        }

        try {
            BankWithdrawMessage bankWithdrawMessage = new Gson().fromJson(message, BankWithdrawMessage.class);
            bankWithdrawService.processWithdraw(bankWithdrawMessage);
        } catch (JsonSyntaxException e) {
            logger.error(MessageFormat.format("[MQ] consume message error, message: {0}", message), e);
        }
    }
}
