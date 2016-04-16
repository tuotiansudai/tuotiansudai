package com.tuotiansudai.api.service;

import com.tuotiansudai.api.dto.BannerResponseDataDto;
import com.tuotiansudai.api.dto.BaseResponseDto;

public interface MobileAppBannerService {
    BaseResponseDto<BannerResponseDataDto> generateBannerList();


    BannerResponseDataDto getLatestBannerInfo(String jsonName);

}
