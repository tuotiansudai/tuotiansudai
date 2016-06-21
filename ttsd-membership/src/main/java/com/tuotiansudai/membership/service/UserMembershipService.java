package com.tuotiansudai.membership.service;

import com.tuotiansudai.membership.repository.model.MembershipModel;
import com.tuotiansudai.membership.repository.model.GivenMembership;
import com.tuotiansudai.membership.repository.model.UserMembershipModel;

public interface UserMembershipService {

    MembershipModel getMembershipByLevel(int level);

    int getProgressBarPercent(String loginName);

    int getExpireDayByLoginName(String loginName);

    UserMembershipModel findByLoginNameByMembershipId(String loginName, long membershipId);

}
