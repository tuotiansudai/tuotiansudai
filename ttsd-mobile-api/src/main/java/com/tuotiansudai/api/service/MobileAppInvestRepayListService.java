package com.tuotiansudai.api.service;


import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.InvestRepayListRequestDto;

public interface MobileAppInvestRepayListService {
    BaseResponseDto generateUserInvestRepayList(InvestRepayListRequestDto requestDto);
}
