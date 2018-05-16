package com.tuotiansudai.mq.consumer.user;

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
public class BankBindCardMessageConsumer implements MessageConsumer {

    private static Logger logger = LoggerFactory.getLogger(BankBindCardMessageConsumer.class);

    private final UserBankCardMapper userBankCardMapper;

    @Autowired
    public BankBindCardMessageConsumer(UserBankCardMapper userBankCardMapper) {
        this.userBankCardMapper = userBankCardMapper;
    }

    @Override
    public MessageQueue queue() {
        return MessageQueue.BindBankCard_Success;
    }

    @Override
    public void consume(String message) {
        logger.info("[MQ] receive message: {}: {}.", this.queue(), message);

        try {
            BankBindCardMessage bankBindCardMessage = new Gson().fromJson(message, BankBindCardMessage.class);

            UserBankCardModel userBankCardModel = userBankCardMapper.findByLoginName(bankBindCardMessage.getLoginName());
            if (userBankCardModel != null) {
                logger.error("[MQ] bank card is exist, message: {}", message);
                return;
            }

            UserBankCardModel model = new UserBankCardModel(bankBindCardMessage.getLoginName(), bankBindCardMessage.getBank(), bankBindCardMessage.getBankCode(), bankBindCardMessage.getCardNumber(), bankBindCardMessage.getBankOrderNo(), bankBindCardMessage.getBankOrderDate(), UserBankCardStatus.BOUND);
            userBankCardMapper.create(model);
        } catch (Exception e) {
            logger.error(MessageFormat.format("[MQ] consume message error, message: {0}", message), e);
        }
    }
}