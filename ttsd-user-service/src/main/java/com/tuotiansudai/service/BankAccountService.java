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
import com.tuotiansudai.repository.mapper.UserBankCardMapper;
import com.tuotiansudai.repository.mapper.UserRoleMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    private final UserBankCardMapper userBankCardMapper;

    @Autowired
    public BankAccountService(UserMapper userMapper, BankAccountMapper bankAccountMapper, MQWrapperClient mqWrapperClient, UserOpLogService userOpLogService, UserRoleMapper userRoleMapper, UserBankCardMapper userBankCardMapper) {
        this.userMapper = userMapper;
        this.bankAccountMapper = bankAccountMapper;
        this.mqWrapperClient = mqWrapperClient;
        this.userOpLogService = userOpLogService;
        this.userRoleMapper = userRoleMapper;
        this.userBankCardMapper = userBankCardMapper;
    }

    public BankAsyncMessage registerInvestorAccount(RegisterAccountDto registerAccountDto, Source source, String token, String ip, String deviceId) {
        BankAccountModel bankAccountModel = bankAccountMapper.findInvestorByLoginName(registerAccountDto.getLoginName());
        if (bankAccountModel != null) {
            return new BankAsyncMessage(null, null, false, "已实名认证");
        }
        userOpLogService.sendUserOpLogMQ(registerAccountDto.getLoginName(), ip, source.name(), deviceId, UserOpType.REGISTER_INVESTOR, null);
        return bankWrapperClient.registerInvestor(source, registerAccountDto.getLoginName(), registerAccountDto.getMobile(), token, registerAccountDto.getUserName(), registerAccountDto.getIdentityNumber());
    }

    public BankAsyncMessage registerLoanerAccount(String loginName, String token, String ip, String deviceId) {
        if (bankAccountMapper.findInvestorByLoginName(loginName) == null){
            return new BankAsyncMessage(null, null, false, "未完成出借人实名认证");
        }

        BankAccountModel bankAccountModel = bankAccountMapper.findLoanerByLoginName(loginName);
        if (bankAccountModel != null) {
            return new BankAsyncMessage(null, null, false, "已实名认证");
        }
        UserModel userModel = userMapper.findByLoginName(loginName);
        userOpLogService.sendUserOpLogMQ(loginName, ip, Source.WEB.name(), deviceId, UserOpType.REGISTER_LOANER, null);
        return bankWrapperClient.registerLoaner(Source.WEB, loginName, userModel.getMobile(), token, userModel.getUserName(), userModel.getIdentityNumber());
    }

    public BankAsyncMessage authorization(Source source, String loginName, String mobile, String ip, String deviceId) {
        BankAccountModel bankAccountModel = bankAccountMapper.findInvestorByLoginName(loginName);
        if (bankAccountModel.isAuthorization()){
            return new BankAsyncMessage(null, null, false, "已开通免密投资");
        }
        userOpLogService.sendUserOpLogMQ(loginName, ip, source.name(), deviceId, UserOpType.INVEST_NO_PASSWORD, null);

        return bankWrapperClient.authorization(source, loginName, mobile, bankAccountModel.getBankUserName(), bankAccountModel.getBankAccountNo());
    }

    public void processBankAccount(BankRegisterMessage bankRegisterMessage){
        if (bankRegisterMessage.isInvestor()){
            this.createInvestorBankAccount(bankRegisterMessage);
        }else{
            this.createLoanerBankAccount(bankRegisterMessage);
        }
    }

    private void createInvestorBankAccount(BankRegisterMessage bankRegisterMessage) {
        String loginName = bankRegisterMessage.getLoginName();

        if (bankAccountMapper.findInvestorByLoginName(loginName) != null) {
            logger.error("investor bank account is existed, message:{} ", new Gson().toJson(bankRegisterMessage));
            return;
        }

        userMapper.updateUserNameAndIdentityNumber(loginName, bankRegisterMessage.getRealName(), bankRegisterMessage.getIdentityCode());

        userRoleMapper.deleteByLoginNameAndRole(loginName, Role.INVESTOR);
        userRoleMapper.create(Lists.newArrayList(new UserRoleModel(loginName, Role.INVESTOR)));

        bankAccountMapper.createInvestor(new BankAccountModel(loginName,
                bankRegisterMessage.getBankUserName(),
                bankRegisterMessage.getBankAccountNo(),
                bankRegisterMessage.getBankOrderNo(),
                bankRegisterMessage.getBankOrderDate()));

        UserBankCardModel model = new UserBankCardModel(loginName, bankRegisterMessage.getBank(), bankRegisterMessage.getBankCode(), bankRegisterMessage.getBankCardNo(), bankRegisterMessage.getBankOrderNo(), bankRegisterMessage.getBankOrderDate(), UserBankCardStatus.BOUND);
        userBankCardMapper.createInvestor(model);

        sendMessage(bankRegisterMessage);
    }

    private void createLoanerBankAccount(BankRegisterMessage bankRegisterMessage) {
        String loginName = bankRegisterMessage.getLoginName();

        if (bankAccountMapper.findLoanerByLoginName(loginName) != null) {
            logger.error("loaner bank account is existed, message:{} ", new Gson().toJson(bankRegisterMessage));
            return;
        }

        userRoleMapper.deleteByLoginNameAndRole(loginName, Role.LOANER);
        userRoleMapper.create(Lists.newArrayList(new UserRoleModel(loginName, Role.LOANER)));

        bankAccountMapper.createLoaner(new BankAccountModel(loginName,
                bankRegisterMessage.getBankUserName(),
                bankRegisterMessage.getBankAccountNo(),
                bankRegisterMessage.getBankOrderNo(),
                bankRegisterMessage.getBankOrderDate()));

        UserBankCardModel model = new UserBankCardModel(loginName, bankRegisterMessage.getBank(), bankRegisterMessage.getBankCode(), bankRegisterMessage.getBankCardNo(), bankRegisterMessage.getBankOrderNo(), bankRegisterMessage.getBankOrderDate(), UserBankCardStatus.BOUND);
        userBankCardMapper.createLoaner(model);

        sendMessage(bankRegisterMessage);
    }

    public void authorizationSuccess(BankAuthorizationMessage bankAuthorizationMessage) {
        BankAccountModel bankAccountModel = bankAccountMapper.findInvestorByLoginName(bankAuthorizationMessage.getLoginName());
        if (bankAccountModel == null) {
            logger.error("[MQ] investor bank account is not exist, message: {}", new Gson().toJson(bankAuthorizationMessage));
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
            MessageEventType type = bankRegisterMessage.isInvestor() ? MessageEventType.REGISTER_INVESTOR_ACCOUNT_SUCCESS : MessageEventType.REGISTER_LOANER_ACCOUNT_SUCCESS;
            mqWrapperClient.sendMessage(MessageQueue.EventMessage, new EventMessage(type,
                    Lists.newArrayList(bankRegisterMessage.getLoginName()),
                    type.getTitleTemplate(),
                    MessageFormat.format(type.getContentTemplate(), bankRegisterMessage.getRealName()),
                    null
            ));

            mqWrapperClient.sendMessage(MessageQueue.PushMessage, new PushMessage(Lists.newArrayList(bankRegisterMessage.getLoginName()),
                    PushSource.ALL,
                    PushType.REGISTER_ACCOUNT_SUCCESS,
                    type.getTitleTemplate(),
                    AppUrl.MESSAGE_CENTER_LIST));
        } catch (Exception e) {
            logger.error("bank register success wechat message notify send fail", e);
        }
    }

    public BankAccountModel findBankAccount(String loginName) {
        return bankAccountMapper.findInvestorByLoginName(loginName);
    }

    public BankAsyncMessage resetPassword(Source source, String loginName) {
        UserModel userModel = userMapper.findByLoginName(loginName);
        BankAccountModel bankAccountModel = bankAccountMapper.findInvestorByLoginName(loginName);
        return bankWrapperClient.resetPassword(source, userModel.getLoginName(), userModel.getMobile(), bankAccountModel.getBankUserName(), bankAccountModel.getBankAccountNo());
    }
}
