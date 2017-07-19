package com.tuotiansudai.api.service.v1_0;

import com.tuotiansudai.api.dto.v1_0.*;

public interface MobileAppCurrentRedeemService {

    BaseResponseDto<CurrentRedeemResponseDataDto> redeem(CurrentRedeemRequestDto redeemRequestDto, String loginName);

    BaseResponseDto<CurrentRedeemLimitResponseDataDto> limitRedeem(String loginName);

}
