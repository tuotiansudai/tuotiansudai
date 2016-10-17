package com.tuotiansudai.service;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.dto.SystemRechargeDto;

public interface SystemRechargeService {

    BaseDto<PayFormDataDto> systemRecharge(SystemRechargeDto systemRechargeDto);
}
