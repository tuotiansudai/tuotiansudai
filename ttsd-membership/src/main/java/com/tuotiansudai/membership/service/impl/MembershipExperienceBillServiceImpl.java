package com.tuotiansudai.membership.service.impl;

import com.tuotiansudai.membership.repository.mapper.MembershipExperienceBillMapper;
import com.tuotiansudai.membership.repository.model.MembershipExperienceBillModel;
import com.tuotiansudai.membership.service.MembershipExperienceBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MembershipExperienceBillServiceImpl implements MembershipExperienceBillService{

    @Autowired
    private MembershipExperienceBillMapper membershipExperienceBillMapper;

    @Override
    public List<MembershipExperienceBillModel> findMembershipExperienceBillList(String loginName, int index, int pageSize) {
        return membershipExperienceBillMapper.findMembershipExperienceBillByLoginName(loginName, (index - 1) * pageSize, pageSize);
    }

    @Override
    public long findMembershipExperienceBillCount(String loginName) {
        return membershipExperienceBillMapper.findMembershipExperienceBillCountByLoginName(loginName);
    }
}
