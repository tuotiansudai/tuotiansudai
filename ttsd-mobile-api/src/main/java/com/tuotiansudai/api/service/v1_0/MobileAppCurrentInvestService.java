package com.tuotiansudai.api.service.v1_0;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.CurrentInvestRequestDto;
import com.tuotiansudai.api.dto.v1_0.InvestNoPassResponseDataDto;
import com.tuotiansudai.api.dto.v1_0.InvestResponseDataDto;

public interface MobileAppCurrentInvestService {

    BaseResponseDto<InvestResponseDataDto> invest(CurrentInvestRequestDto investRequestDto, String loginName);

    BaseResponseDto<InvestNoPassResponseDataDto> noPasswordInvest(CurrentInvestRequestDto investRequestDto, String loginName);
}
