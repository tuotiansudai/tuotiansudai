package com.tuotiansudai.service;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.tuotiansudai.client.BankWrapperClient;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.dto.RegisterAccountDto;
import com.tuotiansudai.enums.*;
import com.tuotiansudai.fudian.message.BankAsyncMessage;
import com.tuotiansudai.fudian.message.BankAuthorizationMessage;
import com.tuotiansudai.fudian.message.BankRegisterMessage;
import com.tuotiansudai.log.service.UserOpLogService;
import com.tuotiansudai.message.EventMessage;
import com.tuotiansudai.message.PushMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.repository.mapper.BankAccountMapper;
import com.tuotiansudai.repository.model.BankAccountModel;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;

@Service
public class BankAccountService {

    private static Logger logger = LoggerFactory.getLogger(BankAccountService.class);

    private final BankWrapperClient bankWrapperClient = new BankWrapperClient();

    private final UserMapper userMapper;

    private final BankAccountMapper bankAccountMapper;

    private final MQWrapperClient mqWrapperClient;

    private final UserOpLogService userOpLogService;

    @Autowired
    public BankAccountService(UserMapper userMapper, BankAccountMapper bankAccountMapper, MQWrapperClient mqWrapperClient, UserOpLogService userOpLogService) {
        this.userMapper = userMapper;
        this.bankAccountMapper = bankAccountMapper;
        this.mqWrapperClient = mqWrapperClient;
        this.userOpLogService = userOpLogService;
    }

    public BankAsyncMessage registerAccount(RegisterAccountDto registerAccountDto, Source source, String ip, String deviceId) {
        BankAccountModel bankAccountModel = bankAccountMapper.findByLoginName(registerAccountDto.getLoginName());
        if (bankAccountModel != null) {
            return new BankAsyncMessage(null, null, false, "已实名认证");
        }
        userOpLogService.sendUserOpLogMQ(registerAccountDto.getLoginName(), ip, source.name(), deviceId, UserOpType.REGISTER, null);
        return bankWrapperClient.register(source, registerAccountDto.getLoginName(), registerAccountDto.getMobile(), registerAccountDto.getUserName(), registerAccountDto.getIdentityNumber());
    }

    @Transactional(rollbackFor = Exception.class)
    public void createBankAccount(BankRegisterMessage bankRegisterMessage) {
        if (bankAccountMapper.findByLoginName(bankRegisterMessage.getLoginName()) != null) {
            logger.info("[MQ] bank register completed bank account, message:{} ", new Gson().toJson(bankRegisterMessage));
            return;
        }
        bankAccountMapper.create(new BankAccountModel(bankRegisterMessage.getLoginName(),
                bankRegisterMessage.getBankUserName(),
                bankRegisterMessage.getBankAccountNo(),
                bankRegisterMessage.getBankOrderNo(),
                bankRegisterMessage.getBankOrderDate()));
        userMapper.updateUserNameAndIdentityNumber(bankRegisterMessage.getLoginName(),
                bankRegisterMessage.getRealName(),
                bankRegisterMessage.getIdentityCode());

        sendMessage(bankRegisterMessage);
    }

    public void authorization(BankAuthorizationMessage bankAuthorizationMessage) {
        BankAccountModel bankAccountModel = bankAccountMapper.findByLoginName(bankAuthorizationMessage.getLoginName());
        if (bankAccountModel == null) {
            logger.error("[MQ] bank account is not exist, message: {}", new Gson().toJson(bankAuthorizationMessage));
            return;
        }
        if (bankAccountModel.isAuthorization()) {
            return;
        }
        bankAccountModel.setAutoInvest(true);
        bankAccountModel.setAuthorization(true);
        bankAccountModel.setBankAuthorizationOrderNo(bankAuthorizationMessage.getBankOrderNo());
        bankAccountModel.setBankAuthorizationOrderDate(bankAuthorizationMessage.getBankOrderDate());
        bankAccountMapper.update(bankAccountModel);
    }

    private void sendMessage(BankRegisterMessage bankRegisterMessage) {
        try {
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
            logger.error("bank register success wechat message notify send fail", e);
        }
    }

    public BankAccountModel findBankAccount(String loginName) {
        return bankAccountMapper.findByLoginName(loginName);
    }

}
