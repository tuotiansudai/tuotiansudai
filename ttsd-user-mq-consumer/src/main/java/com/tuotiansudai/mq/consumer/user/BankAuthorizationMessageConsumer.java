package com.tuotiansudai.mq.consumer.user;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.tuotiansudai.fudian.message.BankAuthorizationMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.service.BankAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class BankAuthorizationMessageConsumer implements MessageConsumer {

    private static Logger logger = LoggerFactory.getLogger(BankAuthorizationMessageConsumer.class);

    private final BankAccountService bankAccountService;

    @Autowired
    public BankAuthorizationMessageConsumer(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @Override
    public MessageQueue queue() {
        return MessageQueue.Authorization_Success;
    }

    @Override
    public void consume(String message) {

        logger.info("[MQ] receive message: {}: {}.", this.queue(), message);

        if (Strings.isNullOrEmpty(message)) {
            logger.error("[MQ] Authorization_Success message is empty");
            return;
        }

        try {
            BankAuthorizationMessage bankAuthorizationMessage = new Gson().fromJson(message, BankAuthorizationMessage.class);
            bankAccountService.authorizationSuccess(bankAuthorizationMessage);
        } catch (JsonSyntaxException e) {
            logger.error(MessageFormat.format("[MQ] consume message error, message: {0}", message), e);
        }

    }
}
