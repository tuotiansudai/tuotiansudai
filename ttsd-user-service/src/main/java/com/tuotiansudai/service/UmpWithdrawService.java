package com.tuotiansudai.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.client.BankWrapperClient;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.enums.*;
import com.tuotiansudai.fudian.message.UmpAsyncMessage;
import com.tuotiansudai.fudian.umpmessage.UmpWithdrawMessage;
import com.tuotiansudai.message.*;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.BankCardMapper;
import com.tuotiansudai.repository.mapper.WithdrawMapper;
import com.tuotiansudai.repository.model.BankCardModel;
import com.tuotiansudai.repository.model.WithdrawModel;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.IdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;

@Service
public class UmpWithdrawService {

    private static Logger logger = LoggerFactory.getLogger(UmpRechargeService.class);

    private final BankWrapperClient bankWrapperClient = new BankWrapperClient();

    private final AccountMapper accountMapper;

    private final BankCardMapper bankCardMapper;

    private final WithdrawMapper withdrawMapper;

    private final MQWrapperClient mqWrapperClient;

    @Autowired
    public UmpWithdrawService(AccountMapper accountMapper, BankCardMapper bankCardMapper, WithdrawMapper withdrawMapper, MQWrapperClient mqWrapperClient) {
        this.accountMapper = accountMapper;
        this.bankCardMapper = bankCardMapper;
        this.withdrawMapper = withdrawMapper;
        this.mqWrapperClient = mqWrapperClient;
    }

    public UmpAsyncMessage withdraw(String loginName, long amount, long withdrawFee) {
        BankCardModel bankCardModel = bankCardMapper.findPassedBankCardByLoginName(loginName);
        WithdrawModel withdrawModel = new WithdrawModel(IdGenerator.generate(), bankCardModel.getId(), loginName, amount, withdrawFee);
        withdrawModel.setFee(withdrawFee);
        withdrawModel.setBankCardId(bankCardModel.getId());
        withdrawModel.setId(IdGenerator.generate());
        return bankWrapperClient.umpWithdraw(loginName, accountMapper.findByLoginName(loginName).getPayUserId(), withdrawModel.getId(), amount - withdrawFee);
    }

    @Transactional
    public void processWithdraw(UmpWithdrawMessage message) {
        WithdrawModel model = withdrawMapper.findById(message.getWithdrawId());
        if (model == null || !Lists.newArrayList(WithdrawStatus.WAIT_PAY, WithdrawStatus.UMP_APPLY_SUCCESS).contains(model.getStatus())) {
            logger.error("UmpWithdrawModel not exist or status is error, isApply:{}, current status:{}, withdrawId: {}", message.isApply(), model.getStatus(), message.getWithdrawId());
        }
        model.setApplyNotifyMessage(message.getApplyMessage());
        model.setNotifyMessage(message.getNotifyMessage());
        if (message.isStatus()) {
            model.setStatus(message.isApply() && model.getStatus() == WithdrawStatus.WAIT_PAY ? WithdrawStatus.UMP_APPLY_SUCCESS : WithdrawStatus.SUCCESS);
        } else {
            model.setStatus(message.isApply() ? WithdrawStatus.UMP_APPLY_FAIL : WithdrawStatus.FAIL);
        }
        withdrawMapper.update(model);

        if (message.isApply()){
            this.apply(message.isStatus(), model);
        }else {
            this.notify(message.isStatus(), model);
        }

        if (message.isStatus()){
            sendMessage(message, model.getAmount());
        }
    }

    private void apply(boolean isSuccess, WithdrawModel model){
        if (isSuccess && model.getStatus() == WithdrawStatus.WAIT_PAY){
            UmpAmountTransferMessage atm = new UmpAmountTransferMessage(UmpTransferType.FREEZE, model.getLoginName(), model.getId(), model.getAmount(), UserBillBusinessType.APPLY_WITHDRAW, null, null);
            mqWrapperClient.sendMessage(MessageQueue.UmpAmountTransfer, atm);
        }
    }

    private void notify(boolean isSuccess, WithdrawModel model){
        if (isSuccess){
            UmpAmountTransferMessage atm = new UmpAmountTransferMessage(model.getStatus() == WithdrawStatus.UMP_APPLY_SUCCESS ?
                    UmpTransferType.TRANSFER_OUT_FREEZE : UmpTransferType.TRANSFER_OUT_BALANCE,
                    model.getLoginName(), model.getAmount(), model.getAmount(), UserBillBusinessType.WITHDRAW_SUCCESS, null, null);
            mqWrapperClient.sendMessage(MessageQueue.UmpAmountTransfer, atm);
        }else if (model.getStatus() == WithdrawStatus.UMP_APPLY_SUCCESS){
            UmpAmountTransferMessage atm = new UmpAmountTransferMessage(UmpTransferType.UNFREEZE, model.getLoginName(), model.getId(), model.getAmount(), UserBillBusinessType.WITHDRAW_FAIL, null, null);
            mqWrapperClient.sendMessage(MessageQueue.AmountTransfer, atm);
        }
    }

    private void sendMessage(UmpWithdrawMessage message, long amount) {
        try {
            String title = message.isApply() ?
                    MessageFormat.format(MessageEventType.WITHDRAW_APPLICATION_SUCCESS.getTitleTemplate(), AmountConverter.convertCentToString(amount)) :
                    MessageFormat.format(MessageEventType.WITHDRAW_SUCCESS.getTitleTemplate(), AmountConverter.convertCentToString(amount));

            String content = message.isApply() ?
                    MessageFormat.format(MessageEventType.WITHDRAW_APPLICATION_SUCCESS.getContentTemplate(), AmountConverter.convertCentToString(amount)) :
                    MessageFormat.format(MessageEventType.WITHDRAW_SUCCESS.getContentTemplate(), AmountConverter.convertCentToString(amount));

            mqWrapperClient.sendMessage(MessageQueue.EventMessage, new EventMessage(message.isApply() ? MessageEventType.WITHDRAW_APPLICATION_SUCCESS : MessageEventType.WITHDRAW_APPLICATION_SUCCESS,
                    Lists.newArrayList(message.getLoginName()),
                    title,
                    content,
                    message.getWithdrawId()));

            mqWrapperClient.sendMessage(MessageQueue.PushMessage, new PushMessage(Lists.newArrayList(message.getLoginName()),
                    PushSource.ALL,
                    message.isApply() ? PushType.WITHDRAW_APPLICATION_SUCCESS : PushType.WITHDRAW_APPLICATION_SUCCESS,
                    title,
                    AppUrl.MESSAGE_CENTER_LIST));

            mqWrapperClient.sendMessage(MessageQueue.WeChatMessageNotify, new WeChatMessageNotify(message.getLoginName(), message.isApply() ? WeChatMessageType.WITHDRAW_APPLY_SUCCESS : WeChatMessageType.WITHDRAW_NOTIFY_SUCCESS, message.getWithdrawId()));

        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }
}
