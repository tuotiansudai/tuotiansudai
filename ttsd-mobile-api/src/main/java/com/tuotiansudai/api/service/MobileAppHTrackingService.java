package com.tuotiansudai.api.service;

import com.tuotiansudai.api.dto.HTrackingRequestDto;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;

public interface MobileAppHTrackingService {

    BaseResponseDto save(HTrackingRequestDto hTrackingRequestDto);
}
