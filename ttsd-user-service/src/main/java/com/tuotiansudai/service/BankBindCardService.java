package com.tuotiansudai.service;

import com.google.common.base.Strings;
import com.tuotiansudai.client.BankWrapperClient;
import com.tuotiansudai.dto.EditUserDto;
import com.tuotiansudai.enums.Role;
import com.tuotiansudai.enums.UserOpType;
import com.tuotiansudai.fudian.message.BankAsyncMessage;
import com.tuotiansudai.fudian.message.BankBindCardMessage;
import com.tuotiansudai.log.service.UserOpLogService;
import com.tuotiansudai.repository.mapper.BankAccountMapper;
import com.tuotiansudai.repository.mapper.BankCardMapper;
import com.tuotiansudai.repository.mapper.UserBankCardMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankBindCardService {

    private static Logger logger = LoggerFactory.getLogger(BankBindCardService.class);

    private final BankWrapperClient bankWrapperClient = new BankWrapperClient();

    private final UserBankCardMapper userBankCardMapper;

    private final UserOpLogService userOpLogService;

    private final UserMapper userMapper;

    private final BankAccountMapper bankAccountMapper;

    @Autowired
    public BankBindCardService(UserMapper userMapper, BankAccountMapper bankAccountMapper, UserBankCardMapper userBankCardMapper, UserOpLogService userOpLogService) {
        this.userMapper = userMapper;
        this.bankAccountMapper = bankAccountMapper;
        this.userBankCardMapper = userBankCardMapper;
        this.userOpLogService = userOpLogService;
    }

    public UserBankCardModel findBankCard(String loginName, Role role) {
        return userBankCardMapper.findByLoginNameAndRole(loginName, role);
    }

    public BankAsyncMessage bind(String loginName, Source source, String ip, String deviceId, Role role) {
        if (role == null) {
            return new BankAsyncMessage("绑卡失败");
        }

        UserBankCardModel userBankCardModel = this.findBankCard(loginName, role);

        if (userBankCardModel != null) {
            return new BankAsyncMessage(null, null, false, "已绑定银行卡");
        }

        // 发送用户行为日志 MQ消息
        userOpLogService.sendUserOpLogMQ(loginName, ip, source.name(), deviceId, UserOpType.BIND_CARD, null);

        UserModel userModel = this.userMapper.findByLoginName(loginName);

        BankAccountModel bankAccountModel = bankAccountMapper.findByLoginNameAndRole(loginName, role);

        if (role == Role.LOANER) {
            return bankWrapperClient.loanerBindBankCard(source, loginName, userModel.getMobile(), bankAccountModel.getBankUserName(), bankAccountModel.getBankAccountNo());
        } else {
            return bankWrapperClient.investorBindBankCard(source, loginName, userModel.getMobile(), bankAccountModel.getBankUserName(), bankAccountModel.getBankAccountNo());
        }
    }

    public void processBind(BankBindCardMessage bankBindCardMessage) {
        UserBankCardModel userBankCardModel = userBankCardMapper.findByLoginNameAndRole(bankBindCardMessage.getLoginName(), bankBindCardMessage.isInvestor() ? Role.INVESTOR : Role.LOANER);
        if (userBankCardModel != null) {
            logger.error("bank card is exist, message: {}", bankBindCardMessage);
            return;
        }

        UserBankCardModel model = new UserBankCardModel(bankBindCardMessage.getLoginName(), bankBindCardMessage.getBank(), bankBindCardMessage.getBankCode(), bankBindCardMessage.getCardNumber(), bankBindCardMessage.getBankOrderNo(), bankBindCardMessage.getBankOrderDate(), UserBankCardStatus.BOUND);
        if (bankBindCardMessage.isInvestor()) {
            userBankCardMapper.createInvestor(model);
        } else {
            userBankCardMapper.createLoaner(model);
        }
    }

    public BankAsyncMessage unbind(String loginName, Source source, String ip, String deviceId, Role role) {

        if (role == null) {
            return new BankAsyncMessage("解绑失败");
        }

        UserBankCardModel userBankCardModel = this.findBankCard(loginName, role);

        if (userBankCardModel == null) {
            return new BankAsyncMessage("未绑定银行卡");
        }

        // 发送用户行为日志 MQ消息
        userOpLogService.sendUserOpLogMQ(loginName, ip, source.name(), deviceId, UserOpType.UNBIND_CARD, null);

        UserModel userModel = this.userMapper.findByLoginName(loginName);

        BankAccountModel bankAccountModel = bankAccountMapper.findByLoginNameAndRole(loginName, role);

        if (role == Role.LOANER) {
            return bankWrapperClient.loanerUnbindBankCard(source, loginName, userModel.getMobile(), bankAccountModel.getBankUserName(), bankAccountModel.getBankAccountNo());
        } else {
            return bankWrapperClient.investorUnbindBankCard(source, loginName, userModel.getMobile(), bankAccountModel.getBankUserName(), bankAccountModel.getBankAccountNo());
        }
    }

    public void processUnbind(BankBindCardMessage bankBindCardMessage) {
        UserBankCardModel userBankCardModel = userBankCardMapper.findByLoginNameAndRole(bankBindCardMessage.getLoginName(), bankBindCardMessage.isInvestor() ? Role.INVESTOR : Role.LOANER);
        if (userBankCardModel == null || Strings.isNullOrEmpty(userBankCardModel.getCardNumber()) || !userBankCardModel.getCardNumber().equalsIgnoreCase(bankBindCardMessage.getCardNumber())) {
            logger.error("[MQ] bank card is not exist, message: {}", bankBindCardMessage);
            return;
        }

        userBankCardMapper.updateStatus(userBankCardModel.getId(), UserBankCardStatus.UNBOUND);
    }


}
