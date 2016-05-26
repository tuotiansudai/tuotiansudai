package com.tuotiansudai.api.service;


import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.ExchangeRequestDto;
import com.tuotiansudai.api.dto.v1_0.UserCouponListResponseDataDto;

public interface MobileAppExchangeService {
    BaseResponseDto<UserCouponListResponseDataDto> exchange(ExchangeRequestDto exchangeRequestDto);
}
