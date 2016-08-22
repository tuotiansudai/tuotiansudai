package com.tuotiansudai.service;

import com.tuotiansudai.repository.model.AccountModel;

public interface AccountService {

    AccountModel findByLoginName(String loginName);

    long getBalance(String loginName);

    boolean isIdentityNumberExist(String identityNumber);

    long getFreeze(String loginName);

    String getRealName(String loginName);
}
