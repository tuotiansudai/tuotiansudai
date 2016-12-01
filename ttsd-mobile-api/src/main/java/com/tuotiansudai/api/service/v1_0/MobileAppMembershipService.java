package com.tuotiansudai.api.service.v1_0;


import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.MembershipRequestDto;
import com.tuotiansudai.api.dto.v1_0.MembershipResponseDataDto;

public interface MobileAppMembershipService {

    BaseResponseDto<MembershipResponseDataDto> getMembershipExperienceBill(MembershipRequestDto requestDto);
}
