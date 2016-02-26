package com.tuotiansudai.api.service;

import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.InvestRequestDto;

public interface MobileAppInvestCouponService {
    BaseResponseDto getInvestCoupons(InvestRequestDto dto);
}
