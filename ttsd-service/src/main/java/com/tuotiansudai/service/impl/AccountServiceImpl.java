package com.tuotiansudai.service.impl;

import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.service.AccountService;
import org.apache.commons.collections4.CollectionUtils;
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
}
