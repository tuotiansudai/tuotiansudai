package com.ttsd.api.service;

import com.ttsd.api.dto.BaseResponseDto;

public interface MobileAppFundManagementService {

    BaseResponseDto queryFundByUserId(String userId);
}
