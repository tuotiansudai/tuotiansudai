package com.tuotiansudai.api.service;


import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.PointBillRequestDto;
import com.tuotiansudai.api.dto.PointTaskRequestDto;

public interface MobileAppPointService {

    BaseResponseDto queryPointBillList(PointBillRequestDto pointBillRequestDto);

    BaseResponseDto queryPointTaskList(PointTaskRequestDto pointTaskRequestDto);
}
