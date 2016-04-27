package com.tuotiansudai.api.service;

import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.ExchangeRequestDto;

public interface MobileAppExchangeService {
    BaseResponseDto exchange(ExchangeRequestDto exchangeRequestDto);
}
