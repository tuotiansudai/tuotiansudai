package com.tuotiansudai.api.service.v1_0;


import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.InvestRepayListRequestDto;

public interface MobileAppInvestRepayListService {
    BaseResponseDto generateUserInvestRepayList(InvestRepayListRequestDto requestDto);
}
