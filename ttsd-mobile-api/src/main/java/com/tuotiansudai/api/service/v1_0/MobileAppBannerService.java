package com.tuotiansudai.api.service.v1_0;

import com.tuotiansudai.api.dto.v1_0.BannerResponseDataDto;
import com.tuotiansudai.api.dto.v1_0.BaseParam;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.repository.model.Source;

public interface MobileAppBannerService {

    BaseResponseDto<BannerResponseDataDto> generateBannerList(String loginName, Source source);

}
