package com.tuotiansudai.api.service;


import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.InvestRequestDto;

public interface MobileAppInvestService {

    BaseResponseDto invest(InvestRequestDto investRequestDto);

    BaseResponseDto noPasswordInvest(InvestRequestDto investRequestDto);
}
