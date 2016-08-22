package com.tuotiansudai.api.service.v1_0;


import com.tuotiansudai.api.dto.v1_0.BankCardRequestDto;
import com.tuotiansudai.api.dto.v1_0.BankLimitRequestDto;
import com.tuotiansudai.api.dto.v1_0.BankLimitResponseDataDto;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;

public interface MobileAppRechargeService {
    BaseResponseDto recharge(BankCardRequestDto bankCardRequestDto);

    BaseResponseDto<BankLimitResponseDataDto> getBankLimit(BankLimitRequestDto bankLimitRequestDto);
}
