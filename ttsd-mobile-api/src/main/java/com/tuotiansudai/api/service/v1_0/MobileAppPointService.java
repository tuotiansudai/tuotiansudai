package com.tuotiansudai.api.service.v1_0;

import com.tuotiansudai.api.dto.v1_0.*;

public interface MobileAppPointService {

    BaseResponseDto<SignInResponseDataDto> signIn(BaseParamDto baseParamDto);

    BaseResponseDto<LastSignInTimeResponseDataDto> getLastSignInTime(BaseParamDto baseParamDto);

    BaseResponseDto<PointBillResponseDataDto> queryPointBillList(PointBillRequestDto pointBillRequestDto);

    BaseResponseDto<PointResponseDataDto> queryPoint(BaseParamDto baseParamDto);

}
