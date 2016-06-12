package com.tuotiansudai.membership.service;

import com.tuotiansudai.membership.repository.model.MembershipExperienceBillModel;

import java.util.Date;
import java.util.List;

public interface MembershipExperienceBillService {

    List<MembershipExperienceBillModel> findMembershipExperienceBillList(String loginName, Date startTime, Date endTime, Integer index, Integer pageSize);

    long  findMembershipExperienceBillCount(String loginName, Date startTime, Date endTime);
}
