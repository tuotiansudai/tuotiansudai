package com.tuotiansudai.api.service;

import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.UserCouponRequestDto;

public interface MobileAppUserCouponService {
    BaseResponseDto getUserCoupons(UserCouponRequestDto dto);
}
