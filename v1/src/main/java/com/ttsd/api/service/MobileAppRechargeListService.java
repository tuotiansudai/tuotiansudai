package com.ttsd.api.service;

import com.ttsd.api.dto.BaseResponseDto;
import com.ttsd.api.dto.RechargeListRequestDto;

public interface MobileAppRechargeListService {

    BaseResponseDto generateRechargeList(RechargeListRequestDto requestDto);
}
