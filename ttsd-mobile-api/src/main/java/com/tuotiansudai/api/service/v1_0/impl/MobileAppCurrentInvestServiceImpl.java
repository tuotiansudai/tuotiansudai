package com.tuotiansudai.api.service.v1_0.impl;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.CurrentInvestRequestDto;
import com.tuotiansudai.api.dto.v1_0.InvestNoPassResponseDataDto;
import com.tuotiansudai.api.dto.v1_0.InvestResponseDataDto;
import com.tuotiansudai.api.service.v1_0.MobileAppCurrentInvestService;

public class MobileAppCurrentInvestServiceImpl implements MobileAppCurrentInvestService {
    @Override
    public BaseResponseDto<InvestResponseDataDto> invest(CurrentInvestRequestDto investRequestDto, String loginName) {
        return null;
    }

    @Override
    public BaseResponseDto<InvestNoPassResponseDataDto> noPasswordInvest(CurrentInvestRequestDto investRequestDto, String loginName) {
        return null;
    }
}
