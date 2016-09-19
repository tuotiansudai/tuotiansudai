package com.tuotiansudai.api.service.v1_0;


import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.MembershipPerceptionRequestDto;

public interface MobileAppMembershipPerceptionService {

    BaseResponseDto getMembershipPerception(MembershipPerceptionRequestDto requestDto);
}
