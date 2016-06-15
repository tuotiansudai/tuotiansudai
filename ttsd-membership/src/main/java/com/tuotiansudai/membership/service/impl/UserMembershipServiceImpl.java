package com.tuotiansudai.membership.service.impl;

import com.tuotiansudai.membership.repository.mapper.MembershipMapper;
import com.tuotiansudai.membership.repository.mapper.UserMembershipMapper;
import com.tuotiansudai.membership.repository.model.*;
import com.tuotiansudai.membership.service.UserMembershipEvaluator;
import com.tuotiansudai.membership.service.UserMembershipService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Calendar;
import java.util.Date;
@Service
public class UserMembershipServiceImpl implements UserMembershipService {

    static Logger logger = Logger.getLogger(UserMembershipServiceImpl.class);

    @Autowired
    private MembershipMapper membershipMapper;

    @Autowired
    private UserMembershipEvaluator userMembershipEvaluator;

    @Autowired
    private UserMembershipMapper userMembershipMapper;

    private static DateTime membershipStartDate = DateTime.parse("2016-07-01");

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

    @Override
    public GivenMembership receiveMembership(String loginName){
        if(Days.daysBetween(DateTime.now(),membershipStartDate).getDays() > 0){
            return GivenMembership.NO_TIME;
        }

        if(loginName == null || loginName.equals("")){
            return GivenMembership.NO_LOGIN;
        }

        if(userMembershipMapper.findAccountIdentityNumberByLoginName(loginName) == 0){
            return GivenMembership.NO_REGISTER;
        }

        if(userMembershipMapper.findByLoginNameByType(loginName,UserMembershipType.GIVEN) != null){
            return GivenMembership.ALREADY_RECEIVED;
        }

        long investAmount = userMembershipMapper.sumSuccessInvestAmountByLoginName(loginName);
        Date registerTime = userMembershipMapper.findAccountRegisterTimeByLoginName(loginName);
        if(registerTime != null && registerTime.getTime() < membershipStartDate.getMillis() && investAmount < 100000){
            return GivenMembership.ALREADY_REGISTER_NOT_INVEST_1000;
        }

        if(registerTime != null && registerTime.getTime() < membershipStartDate.getMillis() && investAmount >= 100000){
            createUserMembershipModel(loginName, MembershipLevel.V5.getLevel());
            return GivenMembership.ALREADY_REGISTER_ALREADY_INVEST_1000;
        }

        return GivenMembership.AFTER_START_ACTIVITY_REGISTER;
    }

    private void createUserMembershipModel(String loginName,int level){
        UserMembershipModel userMembershipModel = new UserMembershipModel(loginName,
                membershipMapper.findByLevel(level).getId(),
                DateTime.now().plusMonths(1).toDate(),
                new Date(),
                UserMembershipType.GIVEN);
        userMembershipMapper.create(userMembershipModel);
    }

}
