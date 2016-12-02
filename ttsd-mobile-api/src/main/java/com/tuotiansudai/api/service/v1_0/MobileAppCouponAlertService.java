package com.tuotiansudai.api.service.v1_0;


import com.tuotiansudai.api.dto.v1_0.BaseParamDto;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.CouponAlertResponseDataDto;

public interface MobileAppCouponAlertService {
    BaseResponseDto<CouponAlertResponseDataDto> getCouponAlert(BaseParamDto baseParamDto);
}
