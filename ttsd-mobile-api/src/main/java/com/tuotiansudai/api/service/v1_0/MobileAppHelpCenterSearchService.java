package com.tuotiansudai.api.service.v1_0;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.HelpCenterSearchListResponseDataDto;
import com.tuotiansudai.api.dto.v1_0.HelpCenterSearchRequestDto;

public interface MobileAppHelpCenterSearchService {
    BaseResponseDto<HelpCenterSearchListResponseDataDto> getHelpCenterSearchResult(HelpCenterSearchRequestDto dto);
}
