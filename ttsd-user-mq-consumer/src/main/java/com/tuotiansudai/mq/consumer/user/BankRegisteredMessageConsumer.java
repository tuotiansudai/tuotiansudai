package com.tuotiansudai.mq.consumer.user;


import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.tuotiansudai.fudian.message.BankRegisterMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.service.BankAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;

@Component
public class BankRegisteredMessageConsumer implements MessageConsumer {

    private static Logger logger = LoggerFactory.getLogger(BankRegisteredMessageConsumer.class);

    private final BankAccountService bankAccountService;

    public BankRegisteredMessageConsumer(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @Override
    public MessageQueue queue() {
        return MessageQueue.RegisterBankAccount_Success;
    }

    @Override
    @Transactional
    public void consume(String message) {
        logger.info("[MQ] receive message: {}: {}.", this.queue(), message);

        if (Strings.isNullOrEmpty(message)) {
            logger.error("[MQ] RegisterBankAccount_Success message is empty");
            return;
        }

        try {
            BankRegisterMessage bankRegisterMessage = new Gson().fromJson(message, BankRegisterMessage.class);
            bankAccountService.createBankAccount(bankRegisterMessage);

        } catch (Exception e) {
            logger.error(MessageFormat.format("[MQ] consume message error, message: {0}", message), e);
        }

    }
}
