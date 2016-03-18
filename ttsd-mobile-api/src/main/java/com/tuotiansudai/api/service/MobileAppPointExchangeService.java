package com.tuotiansudai.api.service;

import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.PointExchangeRequestDto;

public interface MobileAppPointExchangeService {
    BaseResponseDto generatePointExchange(PointExchangeRequestDto pointExchangeRequestDto);

}
