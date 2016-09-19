package com.tuotiansudai.api.service.v1_0;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.MembershipPriceResponseDto;
import com.tuotiansudai.api.dto.v1_0.MembershipPurchaseRequestDto;
import com.tuotiansudai.api.dto.v1_0.MembershipPurchaseResponseDataDto;

public interface MobileAppMembershipPurchaseService {

    BaseResponseDto<MembershipPriceResponseDto> getMembershipPrice();

    BaseResponseDto<MembershipPurchaseResponseDataDto> purchase(MembershipPurchaseRequestDto requestDto);
}
