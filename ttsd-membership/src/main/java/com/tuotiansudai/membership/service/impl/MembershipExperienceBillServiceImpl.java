package com.tuotiansudai.membership.service.impl;

import com.tuotiansudai.membership.repository.mapper.MembershipExperienceBillMapper;
import com.tuotiansudai.membership.repository.model.MembershipExperienceBillModel;
import com.tuotiansudai.membership.service.MembershipExperienceBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class MembershipExperienceBillServiceImpl implements MembershipExperienceBillService{

    @Autowired
    private MembershipExperienceBillMapper membershipExperienceBillMapper;

    @Override
    public List<MembershipExperienceBillModel> findMembershipExperienceBillList(String loginName, Date startTime, Date endTime, Integer index, Integer pageSize) {
        if(index == null && pageSize == null){
            return membershipExperienceBillMapper.findMembershipExperienceBillByLoginName(loginName, startTime, endTime, null, null);
        }
        return membershipExperienceBillMapper.findMembershipExperienceBillByLoginName(loginName, startTime, endTime, (index - 1) * pageSize, pageSize);
    }

    @Override
    public long findMembershipExperienceBillCount(String loginName, Date startTime, Date endTime) {
        return membershipExperienceBillMapper.findMembershipExperienceBillCountByLoginName(loginName, startTime, endTime);
    }
}
