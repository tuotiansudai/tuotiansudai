package com.tuotiansudai.api.service.v1_0;

import com.tuotiansudai.api.dto.v1_0.ActivityCenterResponseDto;
import com.tuotiansudai.repository.model.Source;

public interface MobileAppActivityService {
    ActivityCenterResponseDto getAppActivityCenterResponseData(String loginName, Source source, Integer index, Integer pageSize);
}
