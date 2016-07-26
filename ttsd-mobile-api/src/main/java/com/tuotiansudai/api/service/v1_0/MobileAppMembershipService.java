package com.tuotiansudai.api.service.v1_0;


import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.MembershipRequestDto;

public interface MobileAppMembershipService {

    BaseResponseDto getMembershipExperienceBill(MembershipRequestDto requestDto);
}
