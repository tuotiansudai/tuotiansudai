package com.tuotiansudai.service;

import com.tuotiansudai.client.BankWrapperClient;
import com.tuotiansudai.dto.RegisterAccountDto;
import com.tuotiansudai.enums.UserOpType;
import com.tuotiansudai.fudian.message.BankAsyncMessage;
import com.tuotiansudai.log.service.UserOpLogService;
import com.tuotiansudai.repository.mapper.BankAccountMapper;
import com.tuotiansudai.repository.model.BankAccountModel;
import com.tuotiansudai.repository.model.Source;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BankAccountService {

    private final BankWrapperClient bankWrapperClient = new BankWrapperClient();

    private final BankAccountMapper bankAccountMapper;

    private final UserOpLogService userOpLogService;

    @Autowired
    public BankAccountService(BankAccountMapper bankAccountMapper, UserOpLogService userOpLogService) {
        this.bankAccountMapper = bankAccountMapper;
        this.userOpLogService = userOpLogService;
    }

    public BankAsyncMessage registerAccount(RegisterAccountDto registerAccountDto, Source source, String ip, String deviceId) {
        BankAccountModel bankAccountModel = bankAccountMapper.findByLoginName(registerAccountDto.getLoginName());
//        if (bankAccountModel != null) {
//            return new BankAsyncMessage(null, null, false, "已实名认证");
//        }
        userOpLogService.sendUserOpLogMQ(registerAccountDto.getLoginName(), ip, source.name(), deviceId, UserOpType.REGISTER, null);
        return bankWrapperClient.register(Source.WEB, registerAccountDto.getLoginName(), registerAccountDto.getMobile(), registerAccountDto.getUserName(), registerAccountDto.getIdentityNumber());
    }

    public BankAccountModel findBankAccount(String loginName) {
        return bankAccountMapper.findByLoginName(loginName);
    }
}
