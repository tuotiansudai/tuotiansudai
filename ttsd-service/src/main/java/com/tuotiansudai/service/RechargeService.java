package com.tuotiansudai.service;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.dto.RechargeDto;

public interface RechargeService {

    BaseDto<PayFormDataDto> recharge(RechargeDto rechargeDto);

    String findSumRechargeByLoginName(String loginName);
}
