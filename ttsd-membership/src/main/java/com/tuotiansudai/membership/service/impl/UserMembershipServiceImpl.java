package com.tuotiansudai.membership.service.impl;

import com.tuotiansudai.membership.repository.mapper.MembershipMapper;
import com.tuotiansudai.membership.repository.mapper.UserMembershipMapper;
import com.tuotiansudai.membership.repository.model.MembershipModel;
import com.tuotiansudai.membership.repository.model.UserMembershipModel;
import com.tuotiansudai.membership.service.UserMembershipEvaluator;
import com.tuotiansudai.membership.service.UserMembershipService;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserMembershipServiceImpl implements UserMembershipService {

    @Autowired
    private MembershipMapper membershipMapper;

    @Autowired
    private UserMembershipEvaluator userMembershipEvaluator;

    @Autowired
    private UserMembershipMapper userMembershipMapper;

    @Override
    public MembershipModel getMembershipByLevel(int level) {
        return membershipMapper.findByLevel(level);
    }

    @Override
    public int getProgressBarPercent(String loginName) {
        Long membershipPoint = userMembershipMapper.findMembershipPointByLoginName(loginName);
        MembershipModel membershipModel = userMembershipEvaluator.evaluate(loginName);
        MembershipModel NextLevelMembershipModel = this.getMembershipByLevel(membershipModel.getLevel() >= 5 ? membershipModel.getLevel() : (membershipModel.getLevel() + 1));
        double changeable = ((membershipPoint - membershipModel.getExperience()) / (double) (NextLevelMembershipModel.getExperience() - membershipModel.getExperience())) * 0.2 * 100;
        return (int) (membershipModel.getLevel() * 20 == 100 ? 100 : (membershipModel.getLevel() * 20 + changeable));
    }

    @Override
    public int getExpireDayByLoginName(String loginName) {
        int leftDays = 0;
        MembershipModel membershipModel = userMembershipEvaluator.evaluate(loginName);
        if (membershipModel != null) {
            UserMembershipModel userMembershipModel = userMembershipMapper.findByMembershipId(membershipModel.getId());
            leftDays = Days.daysBetween(new DateTime(), new DateTime(userMembershipModel.getExpiredTime())).getDays();
        }
        return leftDays;
    }
}
