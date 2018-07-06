package com.tuotiansudai.mq.consumer.user;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.client.SignInClient;
import com.tuotiansudai.enums.Role;
import com.tuotiansudai.fudian.message.BankRegisterMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.service.BankAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class BankRegisterMessageConsumer implements MessageConsumer {

    private static Logger logger = LoggerFactory.getLogger(BankRegisterMessageConsumer.class);

    private final SignInClient signInClient = SignInClient.getInstance();

    private final BankAccountService bankAccountService;

    private final MQWrapperClient mqWrapperClient;

    public BankRegisterMessageConsumer(BankAccountService bankAccountService, MQWrapperClient mqWrapperClient) {
        this.bankAccountService = bankAccountService;
        this.mqWrapperClient = mqWrapperClient;
    }

    @Override
    public MessageQueue queue() {
        return MessageQueue.RegisterBankAccount_Success;
    }

    @Override
    public void consume(String message) {
        logger.info("[MQ] receive message: {}: {}.", this.queue(), message);

        if (Strings.isNullOrEmpty(message)) {
            logger.error("[MQ] RegisterBankAccount_Success message is empty");
            return;
        }

        try {
            BankRegisterMessage bankRegisterMessage = new Gson().fromJson(message, BankRegisterMessage.class);
            bankAccountService.processBankAccount(bankRegisterMessage);
            signInClient.switchRole(bankRegisterMessage.getToken(), bankRegisterMessage.isInvestor() ? Role.INVESTOR : Role.LOANER);
            signInClient.refreshData(bankRegisterMessage.getToken(), Source.WEB);
            mqWrapperClient.sendMessage(MessageQueue.RegisterBankAccount_CompletePointTask, message);
        } catch (JsonSyntaxException e) {
            logger.error(MessageFormat.format("[MQ] consume message error, message: {0}", message), e);
        }
    }
}
