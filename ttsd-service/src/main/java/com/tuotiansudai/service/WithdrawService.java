package com.tuotiansudai.service;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.dto.WithdrawDto;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.repository.model.WithdrawModel;

public interface WithdrawService {

    BaseDto<PayFormDataDto> withdraw(WithdrawDto withdrawDto);

    long sumSuccessWithdrawAmount(String loginName);

    WithdrawModel findById(long id);
}
