package com.tuotiansudai.mq.consumer.user;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.tuotiansudai.fudian.message.BankBindCardMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.repository.mapper.UserBankCardMapper;
import com.tuotiansudai.repository.model.UserBankCardModel;
import com.tuotiansudai.repository.model.UserBankCardStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class BankUnbindCardMessageConsumer implements MessageConsumer {

    private static Logger logger = LoggerFactory.getLogger(BankUnbindCardMessageConsumer.class);

    private final UserBankCardMapper userBankCardMapper;

    @Autowired
    public BankUnbindCardMessageConsumer(UserBankCardMapper userBankCardMapper) {
        this.userBankCardMapper = userBankCardMapper;
    }

    @Override
    public MessageQueue queue() {
        return MessageQueue.UnbindBankCard_Success;
    }

    @Override
    public void consume(String message) {
        logger.info("[MQ] receive message: {}: {}.", this.queue(), message);

        try {
            BankBindCardMessage bankBindCardMessage = new Gson().fromJson(message, BankBindCardMessage.class);

            UserBankCardModel userBankCardModel = userBankCardMapper.findByLoginName(bankBindCardMessage.getLoginName());
            if (userBankCardModel == null || Strings.isNullOrEmpty(userBankCardModel.getCardNumber()) || !userBankCardModel.getCardNumber().equalsIgnoreCase(bankBindCardMessage.getCardNumber())) {
                logger.error("[MQ] bank card is not exist, message: {}", message);
                return;
            }

            userBankCardMapper.updateStatus(userBankCardModel.getId(), UserBankCardStatus.UNBOUND);
        } catch (Exception e) {
            logger.error(MessageFormat.format("[MQ] consume message error, message: {0}", message), e);
        }
    }
}