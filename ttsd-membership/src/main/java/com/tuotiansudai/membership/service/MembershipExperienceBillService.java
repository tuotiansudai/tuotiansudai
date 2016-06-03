package com.tuotiansudai.membership.service;

import com.tuotiansudai.membership.repository.model.MembershipExperienceBillModel;
import java.util.List;

public interface MembershipExperienceBillService {

    List<MembershipExperienceBillModel> findMembershipExperienceBillList(String loginName, int index, int pageSize);

    long  findMembershipExperienceBillCount(String loginName);
}
