package com.tuotiansudai.api.service.v1_0;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;

public interface MobileAppFundManagementService {
    BaseResponseDto queryFundByUserId(String userId);
}
