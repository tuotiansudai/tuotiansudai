package com.tuotiansudai.api.service.v1_0;

import com.tuotiansudai.api.dto.v1_0.ActivityCenterResponseDto;

public interface MobileAppActivityService {
    ActivityCenterResponseDto getAppActivityCenterResponseData(String loginName, Integer index, Integer pageSize);
}
