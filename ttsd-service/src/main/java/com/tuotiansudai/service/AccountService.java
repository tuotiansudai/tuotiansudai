package com.tuotiansudai.service;

public interface AccountService {

    long getBalance(String loginName);

    boolean isIdentityNumberExist(String identityNumber);
}
