package com.tuotiansudai.api.service.impl;

import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.service.MobileAppBannerService;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Service;

@Service
public class MobileAppBannerServiceImpl implements MobileAppBannerService {
    @Override
    public BaseResponseDto getAppBanner() {
        throw new NotImplementedException(getClass().getName());
    }
}

