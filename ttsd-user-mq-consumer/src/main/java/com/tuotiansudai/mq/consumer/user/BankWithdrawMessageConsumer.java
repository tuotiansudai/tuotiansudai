package com.tuotiansudai.mq.consumer.user;


import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.enums.TransferType;
import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.enums.WithdrawStatus;
import com.tuotiansudai.fudian.message.BankWithdrawMessage;
import com.tuotiansudai.message.AmountTransferMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.repository.mapper.BankWithdrawMapper;
import com.tuotiansudai.repository.model.BankWithdrawModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;

@Component
public class BankWithdrawMessageConsumer implements MessageConsumer {

    private static Logger logger = LoggerFactory.getLogger(BankWithdrawMessageConsumer.class);

    private final BankWithdrawMapper bankWithdrawMapper;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Autowired
    public BankWithdrawMessageConsumer(BankWithdrawMapper bankWithdrawMapper) {
        this.bankWithdrawMapper = bankWithdrawMapper;
    }

    @Override
    public MessageQueue queue() {
        return MessageQueue.Withdraw_Success;
    }

    @Override
    @Transactional
    public void consume(String message) {
        logger.info("[MQ] receive message: {}: {}.", this.queue(), message);

        if (Strings.isNullOrEmpty(message)) {
            logger.error("[MQ] Withdraw_Success message is empty");
            return;
        }

        try {
            BankWithdrawMessage bankWithdrawMessage = new Gson().fromJson(message, BankWithdrawMessage.class);
            BankWithdrawModel bankWithdrawModel = bankWithdrawMapper.findById(bankWithdrawMessage.getWithdrawId());

            if (bankWithdrawModel.getStatus() != WithdrawStatus.WAIT_PAY) {
                return;
            }

            bankWithdrawModel.setBankCode(bankWithdrawMessage.getBankCode());
            bankWithdrawModel.setBankName(bankWithdrawMessage.getBankName());
            bankWithdrawModel.setCardNumber(bankWithdrawMessage.getCardNumber());
            bankWithdrawModel.setBankOrderNo(bankWithdrawMessage.getBankOrderNo());
            bankWithdrawModel.setBankOrderDate(bankWithdrawMessage.getBankOrderDate());
            bankWithdrawModel.setStatus(bankWithdrawMessage.isStatus() ? WithdrawStatus.SUCCESS : WithdrawStatus.FAIL);
            bankWithdrawMapper.update(bankWithdrawModel);

            if (bankWithdrawMessage.isStatus()) {
                AmountTransferMessage atm = new AmountTransferMessage(TransferType.TRANSFER_OUT_BALANCE, bankWithdrawModel.getLoginName(), bankWithdrawModel.getId(), bankWithdrawModel.getAmount(), UserBillBusinessType.WITHDRAW_SUCCESS, null, null);
                mqWrapperClient.sendMessage(MessageQueue.AmountTransfer, atm);
            }
        } catch (Exception e) {
            logger.error(MessageFormat.format("[MQ] consume message error, message: {0}", message), e);
        }

    }
}
