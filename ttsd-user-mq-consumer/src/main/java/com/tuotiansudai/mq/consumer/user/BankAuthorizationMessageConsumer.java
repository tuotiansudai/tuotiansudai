package com.tuotiansudai.mq.consumer.user;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.tuotiansudai.fudian.message.BankAuthorizationMessage;
import com.tuotiansudai.fudian.message.BankBindCardMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.repository.mapper.BankAccountMapper;
import com.tuotiansudai.repository.model.BankAccountModel;
import com.tuotiansudai.repository.model.UserBankCardModel;
import com.tuotiansudai.repository.model.UserBankCardStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class BankAuthorizationMessageConsumer implements MessageConsumer{

    private static Logger logger = LoggerFactory.getLogger(BankAuthorizationMessageConsumer.class);

    private final BankAccountMapper bankAccountMapper;

    @Autowired
    public  BankAuthorizationMessageConsumer(BankAccountMapper bankAccountMapper){
        this.bankAccountMapper = bankAccountMapper;
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
            BankAccountModel bankAccountModel = bankAccountMapper.findByLoginName(bankAuthorizationMessage.getLoginName());
            if (bankAccountModel == null) {
                logger.error("[MQ] bank account is not exist, message: {}", message);
                return;
            }
            bankAccountModel.setAutoInvest(true);
            bankAccountModel.setAuthorization(true);
            bankAccountModel.setAutoRepay(true);
            bankAccountMapper.update(bankAccountModel);
        } catch (Exception e) {
            logger.error(MessageFormat.format("[MQ] consume message error, message: {0}", message), e);
        }


    }
}
