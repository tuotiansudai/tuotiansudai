package com.tuotiansudai.service;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.repository.model.AccountModel;

public interface AccountService {

    AccountModel findByLoginName(String loginName);

    long getBalance(String loginName);

    boolean isIdentityNumberExist(String identityNumber);

    long getFreeze(String loginName);

    String getRealName(String loginName);

    long getUserPointByLoginName(String loginName);

    String getRealNameByMobile(String mobile);
}
