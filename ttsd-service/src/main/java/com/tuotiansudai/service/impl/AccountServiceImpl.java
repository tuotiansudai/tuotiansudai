package com.tuotiansudai.service.impl;

import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.service.AccountService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2015/9/11.
 */
@Service
public class AccountServiceImpl implements AccountService {

    static Logger logger = Logger.getLogger(AccountServiceImpl.class);

    @Autowired
    private AccountMapper accountMapper;

    @Override
    public AccountModel findByLoginName(String loginName) {
        return accountMapper.findByLoginName(loginName);
    }

}
