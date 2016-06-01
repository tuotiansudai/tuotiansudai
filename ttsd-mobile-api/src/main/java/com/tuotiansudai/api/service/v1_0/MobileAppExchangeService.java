package com.tuotiansudai.api.service.v1_0;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.ExchangeRequestDto;

public interface MobileAppExchangeService {
    BaseResponseDto exchange(ExchangeRequestDto exchangeRequestDto);
}
