package com.tuotiansudai.api.service;


import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.ReferrerListRequestDto;

public interface MobileAppReferrerListService {
    BaseResponseDto generateReferrerList(ReferrerListRequestDto referrerListRequestDto);
}
