package com.tuotiansudai.api.service;


import com.tuotiansudai.api.dto.BankCardRequestDto;
import com.tuotiansudai.api.dto.BaseResponseDto;

public interface MobileAppRechargeService {
    BaseResponseDto recharge(BankCardRequestDto bankCardRequestDto);

}
