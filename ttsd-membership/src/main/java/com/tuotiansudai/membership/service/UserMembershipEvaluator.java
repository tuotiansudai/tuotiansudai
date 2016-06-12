package com.tuotiansudai.membership.service;

import com.tuotiansudai.membership.repository.model.MembershipModel;

import java.text.ParseException;

public interface UserMembershipEvaluator {

    MembershipModel evaluate(String loginName);

    String receiveMembership(String loanName) throws ParseException;
}
