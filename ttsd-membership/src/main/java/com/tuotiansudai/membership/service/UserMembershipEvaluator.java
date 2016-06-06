package com.tuotiansudai.membership.service;

import com.tuotiansudai.membership.repository.model.MembershipModel;

public interface UserMembershipEvaluator {

    MembershipModel evaluate(String loginName);

    MembershipModel getMembershipByLevel(int level);
}
