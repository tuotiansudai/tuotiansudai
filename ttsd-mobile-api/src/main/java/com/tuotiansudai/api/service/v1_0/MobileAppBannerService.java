package com.tuotiansudai.api.service.v1_0;

import com.tuotiansudai.api.dto.v1_0.BannerResponseDataDto;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;

public interface MobileAppBannerService {
    BaseResponseDto<BannerResponseDataDto> generateBannerList();
}
