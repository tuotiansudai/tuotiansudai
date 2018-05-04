package com.tuotiansudai.mq.consumer.user;

import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.repository.mapper.UserBankCardMapper;
import com.tuotiansudai.repository.model.BankCardModel;
import com.tuotiansudai.repository.model.BankCardStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;

@Component
public class BindBankCardMessageConsumer implements MessageConsumer {

    private static Logger logger = LoggerFactory.getLogger(BindBankCardMessageConsumer.class);

    private final UserBankCardMapper userBankCardMapper;

    @Autowired
    public BindBankCardMessageConsumer(UserBankCardMapper userBankCardMapper) {
        this.userBankCardMapper = userBankCardMapper;
    }

    @Override
    public MessageQueue queue() {
        return MessageQueue.BindBankCard_SUCCESS;
    }

    @Transactional
    @Override
    public void consume(String message) {

        logger.info("[MQ] receive message: {}: {}.", this.queue(), message);

        try {
            long orderId = Long.parseLong(callbackRequestModel.getOrderId());
            BankCardModel bankCardModel = bankCardMapper.findById(orderId);
            if (bankCardModel == null) {
                logger.warn(MessageFormat.format("bind bank card order id {0} is not found", String.valueOf(orderId)));
                return;
            }
            if (callbackRequestModel.isSuccess()) {
                bankCardModel.setStatus(BankCardStatus.PASSED);
                String bankCode = callbackRequestModel.getGateId();
                bankCardModel.setBankCode(bankCode);
                bankCardModel.setIsFastPayOn(callbackRequestModel.isOpenPay());
                mqWrapperClient.sendMessage(MessageQueue.BindBankCard_CompletePointTask, bankCardModel.getLoginName());
            } else {
                bankCardModel.setStatus(BankCardStatus.FAILED);
            }
            bankCardMapper.update(bankCardModel);
        } catch (NumberFormatException e) {
            logger.error(MessageFormat.format("bind bank card notify request order {0} is not a number", callbackRequestModel.getOrderId()));
            logger.error(e.getLocalizedMessage(), e);
        }
    }
}