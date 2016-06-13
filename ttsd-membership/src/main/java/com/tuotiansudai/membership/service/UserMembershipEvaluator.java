package com.tuotiansudai.membership.service;

import com.tuotiansudai.membership.repository.model.MembershipModel;
import com.tuotiansudai.membership.repository.model.MembershipType;

import java.text.ParseException;
import java.util.Map;

public interface UserMembershipEvaluator {

    MembershipModel evaluate(String loginName);
}
