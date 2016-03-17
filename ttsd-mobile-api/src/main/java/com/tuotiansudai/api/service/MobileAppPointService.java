package com.tuotiansudai.api.service;

import com.tuotiansudai.api.dto.BaseParamDto;
import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.PointBillRequestDto;
import com.tuotiansudai.api.dto.PointTaskRequestDto;

public interface MobileAppPointService {

    BaseResponseDto signIn(BaseParamDto baseParamDto);

    BaseResponseDto getLastSignInTime(BaseParamDto baseParamDto);

    BaseResponseDto queryPointBillList(PointBillRequestDto pointBillRequestDto);

    BaseResponseDto queryPoint(BaseParamDto baseParamDto);

    BaseResponseDto queryPointTaskList(PointTaskRequestDto pointTaskRequestDto);
}
