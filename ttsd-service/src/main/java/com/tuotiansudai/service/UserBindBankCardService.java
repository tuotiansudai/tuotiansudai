package com.tuotiansudai.service;

import com.tuotiansudai.client.BankWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.enums.UserOpType;
import com.tuotiansudai.fudian.dto.BankAsyncData;
import com.tuotiansudai.log.service.UserOpLogService;
import com.tuotiansudai.repository.mapper.BankAccountMapper;
import com.tuotiansudai.repository.mapper.UserBankCardMapper;
import com.tuotiansudai.repository.model.BankAccountModel;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.repository.model.UserBankCardModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserBindBankCardService {

    private final BankWrapperClient bankWrapperClient = new BankWrapperClient();

    private final UserBankCardMapper userBankCardMapper;

    private final UserOpLogService userOpLogService;

    private final UserMapper userMapper;

    private final BankAccountMapper bankAccountMapper;

    @Autowired
    public UserBindBankCardService(UserMapper userMapper, BankAccountMapper bankAccountMapper, UserBankCardMapper userBankCardMapper, UserOpLogService userOpLogService) {
        this.userMapper = userMapper;
        this.bankAccountMapper = bankAccountMapper;
        this.userBankCardMapper = userBankCardMapper;
        this.userOpLogService = userOpLogService;
    }

    public UserBankCardModel findBankCard(String loginName) {
        return userBankCardMapper.findByLoginName(loginName);
    }

    public BankAsyncData bind(String loginName, Source source, String ip, String deviceId) {
        UserBankCardModel userBankCardModel = userBankCardMapper.findByLoginName(loginName);

        if (userBankCardModel != null) {
            return new BankAsyncData();
        }

        // 发送用户行为日志 MQ消息
        userOpLogService.sendUserOpLogMQ(loginName, ip, source.name(), deviceId, UserOpType.BIND_CARD, null);

        UserModel userModel = this.userMapper.findByLoginName(loginName);

        BankAccountModel bankAccountModel = this.bankAccountMapper.findByLoginName(loginName);

        return bankWrapperClient.bindBankCard(source, loginName, userModel.getMobile(), bankAccountModel.getBankUserName(), bankAccountModel.getBankAccountNo());
    }

    public BankAsyncData unbind(String loginName, Source source, String ip, String deviceId) {
        UserBankCardModel userBankCardModel = userBankCardMapper.findByLoginName(loginName);

//        if (userBankCardModel == null) {
//            payFormDataDto.setMessage("未绑定银行卡");
//            payFormDataDto.setStatus(false);
//            return baseDto;
//        }

        // 发送用户行为日志 MQ消息
        userOpLogService.sendUserOpLogMQ(loginName, ip, source.name(), deviceId, UserOpType.UNBIND_CARD, null);

        UserModel userModel = this.userMapper.findByLoginName(loginName);

        BankAccountModel bankAccountModel = this.bankAccountMapper.findByLoginName(loginName);

        return bankWrapperClient.unbindBankCard(Source.WEB, loginName, userModel.getMobile(), bankAccountModel.getBankUserName(), bankAccountModel.getBankAccountNo());
    }
}
