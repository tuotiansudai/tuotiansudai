package com.tuotiansudai.api.service.v1_0;


import com.tuotiansudai.api.dto.v1_0.*;

public interface MobileAppRechargeService {

    BaseResponseDto<BankAsynResponseDto> recharge(String loginName, BankRechargeRequestDto bankRechargeRequestDto);

    BaseResponseDto<BankLimitResponseDataDto> getBankLimit(BankLimitRequestDto bankLimitRequestDto);
}
