package com.tuotiansudai.api.service.v1_0;

import com.tuotiansudai.api.dto.v1_0.BaseParamDto;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.PointBillRequestDto;

public interface MobileAppPointService {

    BaseResponseDto signIn(BaseParamDto baseParamDto);

    BaseResponseDto getLastSignInTime(BaseParamDto baseParamDto);

    BaseResponseDto queryPointBillList(PointBillRequestDto pointBillRequestDto);

    BaseResponseDto queryPoint(BaseParamDto baseParamDto);

}
