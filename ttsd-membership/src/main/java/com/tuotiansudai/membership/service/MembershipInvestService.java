package com.tuotiansudai.membership.service;

import com.tuotiansudai.membership.repository.model.MembershipModel;

public interface MembershipInvestService {
    void afterInvestSuccess(String loginName, long investAmount, long investId);

    MembershipModel getCurMaxMembership(String loginName);
}
