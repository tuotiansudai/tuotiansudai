package com.tuotiansudai.service;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.dto.RepayDto;

public interface RepayService {

    BaseDto<PayFormDataDto> repay(RepayDto repayDto);
}
