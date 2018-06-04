package com.tuotiansudai.service;

import com.tuotiansudai.client.BankWrapperClient;
import com.tuotiansudai.enums.UserOpType;
import com.tuotiansudai.fudian.message.BankAsyncMessage;
import com.tuotiansudai.log.service.UserOpLogService;
import com.tuotiansudai.repository.mapper.BankAccountMapper;
import com.tuotiansudai.repository.model.BankAccountModel;
import com.tuotiansudai.repository.model.Source;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BankAuthorizationService {

    private final BankAccountMapper bankAccountMapper;

    private final BankWrapperClient bankWrapperClient;

    private final UserOpLogService userOpLogService;

    @Autowired
    public BankAuthorizationService(BankAccountMapper bankAccountMapper, BankWrapperClient bankWrapperClient, UserOpLogService userOpLogService){
        this.bankAccountMapper = bankAccountMapper;
        this.bankWrapperClient = bankWrapperClient;
        this.userOpLogService = userOpLogService;
    }

    public BankAsyncMessage authorization(Source source, String loginName, String mobile, String ip, String deviceId) {
        BankAccountModel bankAccountModel = bankAccountMapper.findByLoginName(loginName);
        if (bankAccountModel.isAuthorization()){
            return new BankAsyncMessage(null, null, false, "已开通免密投资");
        }
        userOpLogService.sendUserOpLogMQ(loginName, ip, source.name(), deviceId, UserOpType.INVEST_NO_PASSWORD, null);

        return bankWrapperClient.authorization(source, loginName, mobile, bankAccountModel.getBankUserName(), bankAccountModel.getBankAccountNo());
    }
}
