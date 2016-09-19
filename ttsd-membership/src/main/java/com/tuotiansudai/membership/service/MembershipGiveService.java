package com.tuotiansudai.membership.service;

import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.membership.dto.MembershipGiveDto;

public interface MembershipGiveService {
    void createAndEditMembershipGive(MembershipGiveDto membershipGiveDto, long usersId);

    BaseDto<BaseDataDto> approveMembershipGive(long id, String validLoginName);

    BaseDto<BaseDataDto> cancelMembershipGive(long id, String validLoginName);
}
