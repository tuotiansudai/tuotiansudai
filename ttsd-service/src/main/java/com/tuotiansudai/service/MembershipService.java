package com.tuotiansudai.service;

import com.tuotiansudai.membership.repository.model.GivenMembership;

public interface MembershipService {

    GivenMembership receiveMembership(String loginName);
}
