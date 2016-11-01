package com.tuotiansudai.api.service.v2_0;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v2_0.UserFundResponseDataDto;

public interface MobileAppUserFundV2Service {
    BaseResponseDto<UserFundResponseDataDto> getUserFund(String loginName);
}
