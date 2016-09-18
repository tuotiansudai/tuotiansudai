package com.tuotiansudai.membership.service;

import com.tuotiansudai.membership.dto.MembershipGiveDto;

public interface MembershipGiveService {
    void createAndEditMembershipGive(MembershipGiveDto membershipGiveDto, long usersId);


}
