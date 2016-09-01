package com.tuotiansudai.api.service.v3_0;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.UserInvestRepayRequestDto;

public interface MobileAppUserInvestRepayV3Service {
    BaseResponseDto userInvestRepay(UserInvestRepayRequestDto userInvestRepayRequestDto);
}
