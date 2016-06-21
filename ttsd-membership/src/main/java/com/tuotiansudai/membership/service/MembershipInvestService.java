package com.tuotiansudai.membership.service;

public interface MembershipInvestService {

    void afterInvestSuccess(String loginName, long investAmount);
}
