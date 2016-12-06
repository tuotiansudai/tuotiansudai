package com.tuotiansudai.api.service.v1_0;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.JpushRequestDto;

public interface MobileAppJpushService {

    BaseResponseDto storeJPushId(String loginName, String source, String jpushId);
}
