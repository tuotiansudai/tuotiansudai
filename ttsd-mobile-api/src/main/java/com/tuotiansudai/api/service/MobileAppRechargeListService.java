package com.tuotiansudai.api.service;


import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.RechargeListRequestDto;

public interface MobileAppRechargeListService {

    BaseResponseDto generateRechargeList(RechargeListRequestDto requestDto);
}
