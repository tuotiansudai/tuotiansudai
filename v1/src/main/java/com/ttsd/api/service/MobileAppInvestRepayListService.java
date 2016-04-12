package com.ttsd.api.service;

import com.ttsd.api.dto.BaseResponseDto;
import com.ttsd.api.dto.InvestRepayListRequestDto;

public interface MobileAppInvestRepayListService {
    BaseResponseDto generateUserInvestRepayList(InvestRepayListRequestDto requestDto);
}
