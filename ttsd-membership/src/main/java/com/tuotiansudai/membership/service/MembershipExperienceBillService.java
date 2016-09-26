package com.tuotiansudai.membership.service;

import com.tuotiansudai.membership.repository.mapper.MembershipExperienceBillMapper;
import com.tuotiansudai.membership.repository.model.MembershipExperienceBillModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class MembershipExperienceBillService {

    @Autowired
    private MembershipExperienceBillMapper membershipExperienceBillMapper;

    public List<MembershipExperienceBillModel> findMembershipExperienceBillList(String loginName, Date startTime, Date endTime, Integer index, Integer pageSize) {
        if (index == null && pageSize == null) {
            return membershipExperienceBillMapper.findMembershipExperienceBillByLoginName(loginName, startTime, endTime, null, null);
        }
        return membershipExperienceBillMapper.findMembershipExperienceBillByLoginName(loginName, startTime, endTime, (index - 1) * pageSize, pageSize);
    }

    public long findMembershipExperienceBillCount(String loginName, Date startTime, Date endTime) {
        return membershipExperienceBillMapper.findMembershipExperienceBillCountByLoginName(loginName, startTime, endTime);
    }
}
