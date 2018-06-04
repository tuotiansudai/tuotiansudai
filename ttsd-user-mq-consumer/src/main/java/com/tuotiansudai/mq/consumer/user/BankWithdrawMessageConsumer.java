package com.tuotiansudai.mq.consumer.user;


import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.enums.*;
import com.tuotiansudai.fudian.message.BankWithdrawMessage;
import com.tuotiansudai.message.AmountTransferMessage;
import com.tuotiansudai.message.EventMessage;
import com.tuotiansudai.message.PushMessage;
import com.tuotiansudai.message.WeChatMessageNotify;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.repository.mapper.BankWithdrawMapper;
import com.tuotiansudai.repository.model.BankWithdrawModel;
import com.tuotiansudai.util.AmountConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class BankWithdrawMessageConsumer implements MessageConsumer {

    private static Logger logger = LoggerFactory.getLogger(BankWithdrawMessageConsumer.class);

    private final BankWithdrawMapper bankWithdrawMapper;

    private final MQWrapperClient mqWrapperClient;

    @Autowired
    public BankWithdrawMessageConsumer(BankWithdrawMapper bankWithdrawMapper, MQWrapperClient mqWrapperClient) {
        this.bankWithdrawMapper = bankWithdrawMapper;
        this.mqWrapperClient = mqWrapperClient;
    }

    @Override
    public MessageQueue queue() {
        return MessageQueue.Withdraw_Success;
    }

    @Override
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

            bankWithdrawModel.setBankOrderNo(bankWithdrawMessage.getBankOrderNo());
            bankWithdrawModel.setBankOrderDate(bankWithdrawMessage.getBankOrderDate());
            bankWithdrawModel.setStatus(bankWithdrawMessage.isStatus() ? WithdrawStatus.SUCCESS : WithdrawStatus.FAIL);
            bankWithdrawMapper.update(bankWithdrawModel);

            if (bankWithdrawMessage.isStatus()) {
                AmountTransferMessage atm = new AmountTransferMessage(TransferType.TRANSFER_OUT_BALANCE, bankWithdrawModel.getLoginName(), bankWithdrawModel.getId(), bankWithdrawModel.getAmount(), UserBillBusinessType.WITHDRAW_SUCCESS);
                mqWrapperClient.sendMessage(MessageQueue.AmountTransfer, atm);

                String title = MessageFormat.format(MessageEventType.WITHDRAW_SUCCESS.getTitleTemplate(), AmountConverter.convertCentToString(bankWithdrawMessage.getAmount()));
                String content = MessageFormat.format(MessageEventType.WITHDRAW_SUCCESS.getContentTemplate(), AmountConverter.convertCentToString(bankWithdrawMessage.getAmount()));
                mqWrapperClient.sendMessage(MessageQueue.EventMessage, new EventMessage(MessageEventType.WITHDRAW_SUCCESS,
                        Lists.newArrayList(bankWithdrawMessage.getLoginName()),
                        title,
                        content,
                        bankWithdrawModel.getId()
                ));
                mqWrapperClient.sendMessage(MessageQueue.PushMessage, new PushMessage(Lists.newArrayList(bankWithdrawMessage.getLoginName()),
                        PushSource.ALL,
                        PushType.WITHDRAW_SUCCESS,
                        title,
                        AppUrl.MESSAGE_CENTER_LIST));
                mqWrapperClient.sendMessage(MessageQueue.WeChatMessageNotify, new WeChatMessageNotify(bankWithdrawMessage.getLoginName(), WeChatMessageType.WITHDRAW_NOTIFY_SUCCESS, bankWithdrawMessage.getWithdrawId()));
            }
        } catch (Exception e) {
            logger.error(MessageFormat.format("[MQ] consume message error, message: {0}", message), e);
        }

    }
}
