package com.tuotiansudai.api.service;

import com.tuotiansudai.api.dto.ShareCallbackRequestDataDto;

public interface MobileAppShareCallbackService {

    boolean shareBannerSuccess(String loginName, ShareCallbackRequestDataDto shareType);
}
