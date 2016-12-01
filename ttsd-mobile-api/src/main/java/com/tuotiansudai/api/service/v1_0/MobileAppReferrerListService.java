package com.tuotiansudai.api.service.v1_0;


import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.ReferrerListRequestDto;
import com.tuotiansudai.api.dto.v1_0.ReferrerListResponseDataDto;

public interface MobileAppReferrerListService {
    BaseResponseDto<ReferrerListResponseDataDto> generateReferrerList(ReferrerListRequestDto referrerListRequestDto);
}
