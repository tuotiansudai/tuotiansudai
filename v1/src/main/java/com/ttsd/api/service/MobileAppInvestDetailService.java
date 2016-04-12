package com.ttsd.api.service;

import com.ttsd.api.dto.BaseResponseDto;
import com.ttsd.api.dto.InvestDetailRequestDto;

public interface MobileAppInvestDetailService {

    BaseResponseDto generateUserInvestDetail(InvestDetailRequestDto requestDto);
}
