package com.tuotiansudai.api.service.v1_0;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.NoPasswordInvestResponseDataDto;

public interface MobileAppNoPasswordInvestService {
    BaseResponseDto<NoPasswordInvestResponseDataDto> getNoPasswordInvestData(String loginName);
}
