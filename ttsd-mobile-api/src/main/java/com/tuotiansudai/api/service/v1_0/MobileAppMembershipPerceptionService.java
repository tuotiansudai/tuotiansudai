package com.tuotiansudai.api.service.v1_0;


import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.MembershipPerceptionRequestDto;
import com.tuotiansudai.api.dto.v1_0.MembershipPerceptionResponseDataDto;

public interface MobileAppMembershipPerceptionService {

    BaseResponseDto<MembershipPerceptionResponseDataDto> getMembershipPerception(MembershipPerceptionRequestDto requestDto);
}
