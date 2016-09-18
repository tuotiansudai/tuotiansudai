package com.tuotiansudai.membership.service.impl;

import com.tuotiansudai.membership.repository.mapper.MembershipExperienceBillMapper;
import com.tuotiansudai.membership.repository.mapper.MembershipMapper;
import com.tuotiansudai.membership.repository.mapper.UserMembershipMapper;
import com.tuotiansudai.membership.repository.model.MembershipExperienceBillModel;
import com.tuotiansudai.membership.repository.model.MembershipModel;
import com.tuotiansudai.membership.repository.model.UserMembershipModel;
import com.tuotiansudai.membership.repository.model.UserMembershipType;
import com.tuotiansudai.membership.service.MembershipInvestService;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class MembershipInvestServiceImpl implements MembershipInvestService {

    private static Logger logger = Logger.getLogger(MembershipInvestServiceImpl.class);

    @Autowired
    MembershipExperienceBillMapper membershipExperienceBillMapper;

    @Autowired
    UserMembershipMapper userMembershipMapper;

    @Autowired
    MembershipMapper membershipMapper;

    @Override
    public void afterInvestSuccess(String loginName, long investAmount, long investId) {

        try {
            long membershipPoint = userMembershipMapper.findMembershipPointByLoginName(loginName);
            long totalPoint = membershipPoint + investAmount / 100;
            userMembershipMapper.updateMembershipPoint(loginName, totalPoint);

            MembershipExperienceBillModel billModel = new MembershipExperienceBillModel(loginName, investAmount/100, totalPoint, new Date(), "您投资了"+investId+"项目" + String.format("%.2f", investAmount / 100D) + "元");
            membershipExperienceBillMapper.create(billModel);

            Integer level = userMembershipMapper.findRealLevelByLoginName(loginName);
            MembershipModel newMembership = membershipMapper.findByExperience(totalPoint);
            if (newMembership.getLevel() > level) {
                UserMembershipModel userMembershipModel = new UserMembershipModel(loginName, newMembership.getId(), new DateTime(9999, 12, 31, 23, 59,59).toDate(), UserMembershipType.UPGRADE);
                userMembershipMapper.create(userMembershipModel);
            }
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public MembershipModel getCurMaxMembership(String loginName) {
        UserMembershipModel userMembershipModel = userMembershipMapper.findCurrentMaxByLoginName(loginName);
        if (null == userMembershipModel) {
            return null;
        }
        return membershipMapper.findById(userMembershipModel.getMembershipId());
    }
}
