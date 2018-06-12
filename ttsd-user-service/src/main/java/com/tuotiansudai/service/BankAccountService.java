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
import com.tuotiansudai.repository.mapper.UserRoleMapper;
import com.tuotiansudai.repository.model.BankAccountModel;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.repository.model.UserRoleModel;
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

    private final UserRoleMapper userRoleMapper;

    @Autowired
    public BankAccountService(UserMapper userMapper, BankAccountMapper bankAccountMapper, MQWrapperClient mqWrapperClient, UserOpLogService userOpLogService, UserRoleMapper userRoleMapper) {
        this.userMapper = userMapper;
        this.bankAccountMapper = bankAccountMapper;
        this.mqWrapperClient = mqWrapperClient;
        this.userOpLogService = userOpLogService;
        this.userRoleMapper = userRoleMapper;
    }

    public BankAsyncMessage registerAccount(RegisterAccountDto registerAccountDto, String token, String ip, String deviceId) {
        BankAccountModel bankAccountModel = bankAccountMapper.findByLoginName(registerAccountDto.getLoginName());
        if (bankAccountModel != null) {
            return new BankAsyncMessage(null, null, false, "已实名认证");
        }
        userOpLogService.sendUserOpLogMQ(registerAccountDto.getLoginName(), ip, registerAccountDto.getSource().name(), deviceId, UserOpType.REGISTER, null);
        return bankWrapperClient.register(registerAccountDto.getSource(), registerAccountDto.getLoginName(), registerAccountDto.getMobile(), token, registerAccountDto.getUserName(), registerAccountDto.getIdentityNumber());
    }

    public BankAsyncMessage authorization(Source source, String loginName, String mobile, String ip, String deviceId) {
        BankAccountModel bankAccountModel = bankAccountMapper.findByLoginName(loginName);
        if (bankAccountModel.isAuthorization()) {
            return new BankAsyncMessage(null, null, false, "已开通免密投资");
        }
        userOpLogService.sendUserOpLogMQ(loginName, ip, source.name(), deviceId, UserOpType.INVEST_NO_PASSWORD, null);

        return bankWrapperClient.authorization(source, loginName, mobile, bankAccountModel.getBankUserName(), bankAccountModel.getBankAccountNo());
    }

    public void createBankAccount(BankRegisterMessage bankRegisterMessage) {
        String loginName = bankRegisterMessage.getLoginName();

        if (bankAccountMapper.findByLoginName(loginName) != null) {
            logger.error("bank account is existed, message:{} ", new Gson().toJson(bankRegisterMessage));
            return;
        }

        userMapper.updateUserNameAndIdentityNumber(loginName, bankRegisterMessage.getRealName(), bankRegisterMessage.getIdentityCode());

        userRoleMapper.deleteByLoginNameAndRole(loginName, Role.INVESTOR);
        userRoleMapper.create(Lists.newArrayList(new UserRoleModel(loginName, Role.INVESTOR)));

        bankAccountMapper.create(new BankAccountModel(loginName,
                bankRegisterMessage.getBankUserName(),
                bankRegisterMessage.getBankAccountNo(),
                bankRegisterMessage.getBankOrderNo(),
                bankRegisterMessage.getBankOrderDate()));

        sendMessage(bankRegisterMessage);
    }

    public void authorizationSuccess(BankAuthorizationMessage bankAuthorizationMessage) {
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
