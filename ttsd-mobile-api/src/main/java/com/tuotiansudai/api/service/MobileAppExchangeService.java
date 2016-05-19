package com.tuotiansudai.api.service;

import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.ExchangeRequestDto;
import com.tuotiansudai.api.dto.UserCouponListResponseDataDto;

public interface MobileAppExchangeService {
    BaseResponseDto<UserCouponListResponseDataDto> exchange(ExchangeRequestDto exchangeRequestDto);
}
