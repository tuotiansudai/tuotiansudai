package com.tuotiansudai.membership.service;

import com.tuotiansudai.membership.repository.model.MembershipModel;
import com.tuotiansudai.membership.repository.model.MembershipType;

import java.text.ParseException;
import java.util.List;

public interface UserMembershipService {

    MembershipModel getMembershipByLevel(int level);

    int getProgressBarPercent(String loginName);

    int getExpireDayByLoginName(String loginName);

    MembershipType receiveMembership(String loanName);

}
