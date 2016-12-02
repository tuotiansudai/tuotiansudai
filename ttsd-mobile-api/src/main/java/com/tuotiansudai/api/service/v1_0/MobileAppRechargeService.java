package com.tuotiansudai.api.service.v1_0;


import com.tuotiansudai.api.dto.v1_0.*;

public interface MobileAppRechargeService {
    BaseResponseDto<BankCardResponseDto> recharge(BankCardRequestDto bankCardRequestDto);

    BaseResponseDto<BankLimitResponseDataDto> getBankLimit(BankLimitRequestDto bankLimitRequestDto);
}
