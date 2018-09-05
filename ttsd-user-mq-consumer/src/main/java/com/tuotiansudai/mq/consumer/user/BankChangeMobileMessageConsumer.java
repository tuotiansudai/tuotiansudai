package com.tuotiansudai.mq.consumer.user;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.tuotiansudai.fudian.message.BankAuthorizationMessage;
import com.tuotiansudai.fudian.message.BankChangeMobileMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.service.BankAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class BankChangeMobileMessageConsumer implements MessageConsumer {

    private static Logger logger = LoggerFactory.getLogger(BankChangeMobileMessageConsumer.class);

    @Autowired
    private  BankAccountService bankAccountService;



    @Override
    public MessageQueue queue() {
        return MessageQueue.BankChangeMobile;
    }

    @Override
    public void consume(String message) {

        logger.info("[MQ] receive message: {}: {}.", this.queue(), message);

        if (Strings.isNullOrEmpty(message)) {
            logger.error("[MQ] Bank_change_mobile message is empty");
            return;
        }

        try {
            BankChangeMobileMessage bankChangeMobileMessage = new Gson().fromJson(message, BankChangeMobileMessage.class);
            bankAccountService.processChangeBankMobile(bankChangeMobileMessage);
        } catch (JsonSyntaxException e) {
            logger.error(MessageFormat.format("[MQ] consume message error, message: {0}", message), e);
        }

    }
}
