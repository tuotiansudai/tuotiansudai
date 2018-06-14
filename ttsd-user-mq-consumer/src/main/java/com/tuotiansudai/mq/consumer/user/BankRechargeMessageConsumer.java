package com.tuotiansudai.mq.consumer.user;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.enums.BankRechargeStatus;
import com.tuotiansudai.enums.TransferType;
import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.fudian.message.BankRechargeMessage;
import com.tuotiansudai.message.AmountTransferMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.repository.mapper.BankRechargeMapper;
import com.tuotiansudai.repository.model.BankRechargeModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class BankRechargeMessageConsumer implements MessageConsumer {
    private static Logger logger = LoggerFactory.getLogger(BankRechargeMessageConsumer.class);

    @Autowired
    private BankRechargeMapper bankRechargeMapper;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Override
    public MessageQueue queue() {
        return MessageQueue.Recharge_Success;
    }

    @Override
    public void consume(String message) {
        logger.info("[MQ] receive message: {}: {}.", this.queue(), message);

        if (Strings.isNullOrEmpty(message)) {
            logger.error("[MQ] Recharge_Success message is empty");
            return;
        }
        try {
            BankRechargeMessage bankRechargeMessage = new Gson().fromJson(message, BankRechargeMessage.class);
            BankRechargeModel userRechargeModel = bankRechargeMapper.findById(bankRechargeMessage.getRechargeId());

            if (userRechargeModel.getStatus() != BankRechargeStatus.WAIT_PAY) {
                logger.error("[MQ] receive message : {}, userRechargeModel statue is not wait rechargeId:{} ", this.queue(), bankRechargeMessage.getRechargeId());
                return;
            }
            userRechargeModel.setStatus(bankRechargeMessage.isStatus() ? BankRechargeStatus.SUCCESS : BankRechargeStatus.FAIL);
            userRechargeModel.setBankOrderNo(bankRechargeMessage.getBankOrderNo());
            userRechargeModel.setBankOrderDate(bankRechargeMessage.getBankOrderDate());
            bankRechargeMapper.update(userRechargeModel);

            if (bankRechargeMessage.isStatus()) {
                mqWrapperClient.sendMessage(MessageQueue.AmountTransfer,
                        new AmountTransferMessage(TransferType.TRANSFER_IN_BALANCE,
                                bankRechargeMessage.getLoginName(),
                                bankRechargeMessage.getRechargeId(),
                                userRechargeModel.getAmount(),
                                UserBillBusinessType.RECHARGE_SUCCESS));
            }

        } catch (Exception e) {
            logger.error(MessageFormat.format("[MQ] consume message error, message: {0}", message), e);
        }

    }

}