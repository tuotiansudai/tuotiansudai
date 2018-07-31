package com.tuotiansudai.api.service.v1_0;


import com.tuotiansudai.api.dto.v1_0.*;

public interface MobileAppInvestService {

    BaseResponseDto<BankAsynResponseDto> invest(InvestRequestDto investRequestDto);

    BaseResponseDto<BankAsynResponseDto> noPasswordInvest(InvestRequestDto investRequestDto);
}
