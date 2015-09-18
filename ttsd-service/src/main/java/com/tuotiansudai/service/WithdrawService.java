package com.tuotiansudai.service;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.dto.WithdrawDto;

public interface WithdrawService {
    BaseDto<PayFormDataDto> withdraw(WithdrawDto withdrawDto);

    String findSumWithdrawByLoginName(String loginName);
}
