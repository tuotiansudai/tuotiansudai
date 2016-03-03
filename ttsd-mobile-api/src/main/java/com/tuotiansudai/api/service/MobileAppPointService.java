package com.tuotiansudai.api.service;


import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.PointBillRequestDto;

public interface MobileAppPointService {

    BaseResponseDto queryPointBillList(PointBillRequestDto pointBillRequestDto);
}
