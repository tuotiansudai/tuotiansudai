package com.tuotiansudai.api.service;

import com.tuotiansudai.api.dto.BaseResponseDto;

public interface MobileAppFundManagementService {
    BaseResponseDto queryFundByUserId(String userId);
}
