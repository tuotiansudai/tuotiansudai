package com.tuotiansudai.membership.service;

import com.tuotiansudai.membership.repository.model.MembershipModel;
import com.tuotiansudai.membership.repository.model.GivenMembership;

public interface UserMembershipService {

    MembershipModel getMembershipByLevel(int level);

    int getProgressBarPercent(String loginName);

    int getExpireDayByLoginName(String loginName);

    GivenMembership receiveMembership(String loanName);

}
