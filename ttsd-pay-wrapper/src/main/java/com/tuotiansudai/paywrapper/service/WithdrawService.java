package com.tuotiansudai.paywrapper.service;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.dto.WithdrawDto;
import org.springframework.transaction.annotation.Transactional;

public interface WithdrawService {
    @Transactional
    BaseDto<PayFormDataDto> withdraw(WithdrawDto dto);
}
