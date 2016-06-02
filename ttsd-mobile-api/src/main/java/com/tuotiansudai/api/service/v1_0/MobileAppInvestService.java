package com.tuotiansudai.api.service.v1_0;


import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.InvestRequestDto;

public interface MobileAppInvestService {

    BaseResponseDto invest(InvestRequestDto investRequestDto);

    BaseResponseDto noPasswordInvest(InvestRequestDto investRequestDto);
}
