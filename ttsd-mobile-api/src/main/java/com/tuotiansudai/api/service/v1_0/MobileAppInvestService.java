package com.tuotiansudai.api.service.v1_0;


import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.InvestNoPassResponseDataDto;
import com.tuotiansudai.api.dto.v1_0.InvestRequestDto;
import com.tuotiansudai.api.dto.v1_0.InvestResponseDataDto;

public interface MobileAppInvestService {

    BaseResponseDto<InvestResponseDataDto> invest(InvestRequestDto investRequestDto);

    BaseResponseDto<InvestNoPassResponseDataDto> noPasswordInvest(InvestRequestDto investRequestDto);
}
