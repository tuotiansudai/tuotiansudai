package com.tuotiansudai.api.service.v1_0;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.InvestRequestDto;
import com.tuotiansudai.api.dto.v1_0.UserCouponListResponseDataDto;

public interface MobileAppInvestCouponService {
    BaseResponseDto<UserCouponListResponseDataDto> getInvestCoupons(InvestRequestDto dto);
}
