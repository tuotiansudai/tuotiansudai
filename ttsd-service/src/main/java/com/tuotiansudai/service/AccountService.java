package com.tuotiansudai.service;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.HuiZuDataDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.RegisterAccountDto;
import com.tuotiansudai.repository.model.AccountModel;

public interface AccountService {

    BaseDto<PayDataDto> registerAccount(RegisterAccountDto dto);

    boolean resetUmpayPassword(String loginName, String identityNumber);

    AccountModel findByLoginName(String loginName);

    long getBalance(String loginName);

    long getFreeze(String loginName);

    BaseDto<HuiZuDataDto> registerAccountFromHuiZu(RegisterAccountDto dto) ;
}
