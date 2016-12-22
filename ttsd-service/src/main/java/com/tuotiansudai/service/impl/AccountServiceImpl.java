package com.tuotiansudai.service.impl;

import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.RegisterAccountDto;
import com.tuotiansudai.dto.ResetUmpayPasswordDto;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.service.AccountService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {

    static Logger logger = Logger.getLogger(AccountServiceImpl.class);

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Override
    public BaseDto<PayDataDto> registerAccount(RegisterAccountDto dto) {
        return payWrapperClient.register(dto);
    }

    public AccountModel findByLoginName(String loginName) {
        return accountMapper.findByLoginName(loginName);
    }

    public long getBalance(String loginName) {
        AccountModel accountModel = accountMapper.findByLoginName(loginName);
        return accountModel != null ? accountModel.getBalance() : 0;
    }

    @Override
    public long getFreeze(String loginName) {
        AccountModel accountModel = accountMapper.findByLoginName(loginName);
        return accountModel != null ? accountModel.getFreeze() : 0;
    }

    @Override
    public long getUserPointByLoginName(String loginName) {
        AccountModel accountModel = accountMapper.findByLoginName(loginName);
        return null == accountModel ? 0 : accountModel.getPoint();
    }

    @Override
    public boolean resetUmpayPassword(String loginName, String identityNumber) {
        UserModel userModel = userMapper.findByLoginName(loginName);
        if (userModel == null || !userModel.getIdentityNumber().equals(identityNumber)) {
            return false;
        }
        ResetUmpayPasswordDto resetUmpayPasswordDto = new ResetUmpayPasswordDto(loginName, identityNumber);
        return payWrapperClient.resetUmpayPassword(resetUmpayPasswordDto);
    }
}
