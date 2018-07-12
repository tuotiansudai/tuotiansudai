package com.tuotiansudai.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.client.BankWrapperClient;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.enums.*;
import com.tuotiansudai.fudian.message.BankAsyncMessage;
import com.tuotiansudai.fudian.message.BankWithdrawMessage;
import com.tuotiansudai.message.AmountTransferMessage;
import com.tuotiansudai.message.EventMessage;
import com.tuotiansudai.message.PushMessage;
import com.tuotiansudai.message.WeChatMessageNotify;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.repository.mapper.BankAccountMapper;
import com.tuotiansudai.repository.mapper.BankWithdrawMapper;
import com.tuotiansudai.repository.mapper.WeChatUserMapper;
import com.tuotiansudai.repository.model.BankAccountModel;
import com.tuotiansudai.repository.model.BankWithdrawModel;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.repository.model.WeChatUserModel;
import com.tuotiansudai.util.AmountConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Optional;

@Service
public class BankWithdrawService {

    private static Logger logger = LoggerFactory.getLogger(BankWithdrawService.class);

    private final BankWrapperClient bankWrapperClient;

    private final MQWrapperClient mqWrapperClient;

    private final BankWithdrawMapper bankWithdrawMapper;

    private final BankAccountMapper bankAccountMapper;

    private final WeChatUserMapper weChatUserMapper;

    @Autowired
    public BankWithdrawService(BankWithdrawMapper bankWithdrawMapper, BankAccountMapper bankAccountMapper, WeChatUserMapper weChatUserMapper, MQWrapperClient mqWrapperClient) {
        this.bankWrapperClient = new BankWrapperClient();
        this.bankWithdrawMapper = bankWithdrawMapper;
        this.bankAccountMapper = bankAccountMapper;
        this.weChatUserMapper = weChatUserMapper;
        this.mqWrapperClient = mqWrapperClient;
    }

    public BankAsyncMessage withdraw(Source source, String loginName, String mobile, long amount, long fee, Role role) {
        if (role == null){
            return new BankAsyncMessage("提现失败");
        }

        BankAccountModel bankAccountModel = bankAccountMapper.findByLoginNameAndRole(loginName, role);
        BankWithdrawModel bankWithdrawModel = new BankWithdrawModel(loginName, amount, fee, source);

        if (role == Role.LOANER){
            bankWithdrawMapper.createLoaner(bankWithdrawModel);
        }else {
            bankWithdrawMapper.createInvestor(bankWithdrawModel);
        }

        Optional<WeChatUserModel> optional = weChatUserMapper.findByLoginName(loginName).stream().filter(WeChatUserModel::isBound).findFirst();
        return bankWrapperClient.withdraw(bankWithdrawModel.getId(), source, loginName, mobile, bankAccountModel.getBankUserName(), bankAccountModel.getBankAccountNo(), amount, fee, optional.map(WeChatUserModel::getOpenid).orElse(null));
    }

    @Transactional
    public void processWithdraw(BankWithdrawMessage bankWithdrawMessage) {
        BankWithdrawModel bankWithdrawModel = bankWithdrawMapper.findById(bankWithdrawMessage.getWithdrawId());

        if (bankWithdrawModel.getStatus() != WithdrawStatus.WAIT_PAY) {
            return;
        }

        bankWithdrawModel.setBankOrderNo(bankWithdrawMessage.getBankOrderNo());
        bankWithdrawModel.setBankOrderDate(bankWithdrawMessage.getBankOrderDate());
        bankWithdrawModel.setStatus(bankWithdrawMessage.isStatus() ? WithdrawStatus.SUCCESS : WithdrawStatus.FAIL);
        bankWithdrawMapper.update(bankWithdrawModel);

        if (bankWithdrawMessage.isStatus()) {
            mqWrapperClient.sendMessage(MessageQueue.AmountTransfer,
                    Lists.newArrayList(new AmountTransferMessage(
                            bankWithdrawModel.getId(),
                            bankWithdrawModel.getLoginName(),
                            bankWithdrawModel.getRoleType(),
                            bankWithdrawModel.getAmount(),
                            bankWithdrawMessage.getBankOrderNo(),
                            bankWithdrawMessage.getBankOrderDate(),
                            BankUserBillOperationType.OUT,
                            BankUserBillBusinessType.WITHDRAW_SUCCESS)));

            try {
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
            } catch (Exception e) {
                logger.error(e.getLocalizedMessage(), e);
            }
        }
    }

    public BankWithdrawModel findById(long id) {
        return bankWithdrawMapper.findById(id);
    }

    public long sumSuccessWithdrawByLoginName(String loginName, Role role) {
        return bankWithdrawMapper.sumSuccessWithdrawByLoginNameAndRole(loginName, role);
    }
}
