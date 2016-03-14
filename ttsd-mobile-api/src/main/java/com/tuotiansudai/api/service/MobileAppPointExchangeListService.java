package com.tuotiansudai.api.service;

import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.PointExchangeListRequestDto;

public interface MobileAppPointExchangeListService {
    BaseResponseDto generatePointExchangeList(PointExchangeListRequestDto pointExchangeListRequestDto);

}
