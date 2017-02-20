package com.tuotiansudai.api.service.v1_0;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.MembershipPrivilegePriceResponseDto;
import com.tuotiansudai.api.dto.v1_0.MembershipPrivilegePurchaseRequestDto;
import com.tuotiansudai.api.dto.v1_0.MembershipPrivilegePurchaseResponseDataDto;

public interface MobileAppMembershipPrivilegePurchaseService {
    MembershipPrivilegePriceResponseDto obtainMembershipPrivilegePrices();

    BaseResponseDto<MembershipPrivilegePurchaseResponseDataDto> purchase(MembershipPrivilegePurchaseRequestDto requestDto);


}
