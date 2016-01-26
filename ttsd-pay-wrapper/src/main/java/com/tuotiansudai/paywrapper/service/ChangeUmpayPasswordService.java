package com.tuotiansudai.paywrapper.service;


import com.tuotiansudai.dto.AccountDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;

public interface ChangeUmpayPasswordService {

    BaseDto<PayDataDto> changeUmpayPassword(AccountDto accountDto);
}
