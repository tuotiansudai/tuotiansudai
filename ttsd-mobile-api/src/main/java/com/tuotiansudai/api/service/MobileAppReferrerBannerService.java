package com.tuotiansudai.api.service;


import com.tuotiansudai.api.dto.BannerResponseDataDto;
import com.tuotiansudai.api.dto.BaseResponseDto;

public interface MobileAppReferrerBannerService {

    BaseResponseDto<BannerResponseDataDto> generateReferrerBannerList();
}
