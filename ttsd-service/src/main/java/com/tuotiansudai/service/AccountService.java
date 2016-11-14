package com.tuotiansudai.service;

import com.tuotiansudai.repository.model.AccountModel;

public interface AccountService {

    AccountModel findByLoginName(String loginName);

    long getBalance(String loginName);

    long getFreeze(String loginName);

    long getUserPointByLoginName(String loginName);
}
