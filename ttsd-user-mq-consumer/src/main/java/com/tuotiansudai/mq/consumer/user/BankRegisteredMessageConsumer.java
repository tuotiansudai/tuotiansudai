package com.tuotiansudai.mq.consumer.user;


import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.enums.AppUrl;
import com.tuotiansudai.enums.MessageEventType;
import com.tuotiansudai.enums.PushSource;
import com.tuotiansudai.enums.PushType;
import com.tuotiansudai.fudian.message.BankRegisterMessage;
import com.tuotiansudai.message.EventMessage;
import com.tuotiansudai.message.PushMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.repository.mapper.BankAccountMapper;
import com.tuotiansudai.repository.model.BankAccountModel;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;

@Component
public class BankRegisteredMessageConsumer implements MessageConsumer {

    private static Logger logger = LoggerFactory.getLogger(BankRegisteredMessageConsumer.class);

    private final UserMapper userMapper;

    private final BankAccountMapper bankAccountMapper;

    private final MQWrapperClient mqWrapperClient;

    public BankRegisteredMessageConsumer(UserMapper userMapper, BankAccountMapper bankAccountMapper, MQWrapperClient mqWrapperClient){
        this.userMapper = userMapper;
        this.bankAccountMapper = bankAccountMapper;
        this.mqWrapperClient = mqWrapperClient;
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
            if (bankAccountMapper.findByLoginName(bankRegisterMessage.getLoginName()) != null) {
                logger.info("[MQ] receive message: {}, user:{} completed bank account ", this.queue(), bankRegisterMessage.getLoginName());
                return;
            }
            bankAccountMapper.create(new BankAccountModel(bankRegisterMessage.getLoginName(),
                    bankRegisterMessage.getBankUserName(),
                    bankRegisterMessage.getBankAccountNo(),
                    bankRegisterMessage.getBankOrderNo(),
                    bankRegisterMessage.getBankOrderDate()));
            userMapper.updateUserNameAndIdentityNumber(bankRegisterMessage.getLoginName(), bankRegisterMessage.getRealName(), bankRegisterMessage.getIdentityCode());


            mqWrapperClient.sendMessage(MessageQueue.EventMessage, new EventMessage(MessageEventType.REGISTER_ACCOUNT_SUCCESS,
                    Lists.newArrayList(bankRegisterMessage.getLoginName()),
                    MessageEventType.REGISTER_ACCOUNT_SUCCESS.getTitleTemplate(),
                    MessageFormat.format(MessageEventType.REGISTER_ACCOUNT_SUCCESS.getContentTemplate(), bankRegisterMessage.getRealName()),
                    null
            ));

            mqWrapperClient.sendMessage(MessageQueue.PushMessage, new PushMessage(Lists.newArrayList(bankRegisterMessage.getLoginName()),
                    PushSource.ALL,
                    PushType.REGISTER_ACCOUNT_SUCCESS,
                    MessageEventType.REGISTER_ACCOUNT_SUCCESS.getTitleTemplate(),
                    AppUrl.MESSAGE_CENTER_LIST));

        } catch (Exception e) {
            logger.error(MessageFormat.format("[MQ] consume message error, message: {0}", message), e);
        }

    }
}
