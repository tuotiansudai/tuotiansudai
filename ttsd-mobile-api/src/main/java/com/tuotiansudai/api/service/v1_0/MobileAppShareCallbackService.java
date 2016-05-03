package com.tuotiansudai.api.service.v1_0;

import com.tuotiansudai.api.dto.v1_0.ShareCallbackRequestDataDto;

public interface MobileAppShareCallbackService {

    boolean shareBannerSuccess(String loginName, ShareCallbackRequestDataDto shareType);
}
