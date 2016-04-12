package com.tuotiansudai.api.service;

import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.InvestDetailRequestDto;

public interface MobileAppInvestDetailService {

    BaseResponseDto generateUserInvestDetail(InvestDetailRequestDto requestDto);
}
