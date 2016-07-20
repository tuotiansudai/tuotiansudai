package com.tuotiansudai.api.service.v1_0;

import com.tuotiansudai.api.dto.v1_0.*;

public interface MobileAppPointService {

    BaseResponseDto signIn(BaseParamDto baseParamDto);

    BaseResponseDto getLastSignInTime(BaseParamDto baseParamDto);

    BaseResponseDto queryPointBillList(PointBillRequestDto pointBillRequestDto);

    BaseResponseDto queryPoint(BaseParamDto baseParamDto);

    BaseResponseDto<PointTaskListResponseDataDto> queryPointTaskList(PointTaskRequestDto pointTaskRequestDto);
}
