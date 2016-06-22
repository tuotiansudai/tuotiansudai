package com.tuotiansudai.service.impl;

import com.tuotiansudai.membership.repository.mapper.MembershipMapper;
import com.tuotiansudai.membership.repository.mapper.UserMembershipMapper;
import com.tuotiansudai.membership.repository.model.GivenMembership;
import com.tuotiansudai.membership.repository.model.MembershipLevel;
import com.tuotiansudai.membership.repository.model.UserMembershipModel;
import com.tuotiansudai.membership.repository.model.UserMembershipType;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.service.MembershipService;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class MembershipServiceImpl implements MembershipService {

    @Value("#{'${web.heroRanking.activity.period}'.split('\\~')}")
    private List<String> heroRankingActivityPeriod;

    static Logger logger = Logger.getLogger(MembershipServiceImpl.class);

    @Autowired
    private MembershipMapper membershipMapper;

    @Autowired
    private UserMembershipMapper userMembershipMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Override
    public GivenMembership receiveMembership(String loginName){
        if(DateTime.parse(heroRankingActivityPeriod.get(0), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate().after(DateTime.now().toDate())){
            return GivenMembership.NO_TIME;
        }

        if(DateTime.parse(heroRankingActivityPeriod.get(1),DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate().before(DateTime.now().toDate())){
            return GivenMembership.END_TIME;
        }

        if(loginName == null || loginName.equals("")){
            return GivenMembership.NO_LOGIN;
        }

        if(userMembershipMapper.findAccountIdentityNumberByLoginName(loginName) == 0){
            return GivenMembership.NO_REGISTER;
        }

        if(userMembershipMapper.findByLoginNameByType(loginName, UserMembershipType.GIVEN) != null){
            return GivenMembership.ALREADY_RECEIVED;
        }

        long investAmount = investMapper.sumSuccessInvestAmountByLoginName(null,loginName);
        Date registerTime = accountMapper.findAccountRegisterTimeByLoginName(loginName);
        if(registerTime != null && DateTime.parse(heroRankingActivityPeriod.get(0),DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate().after(registerTime) && investAmount < 1000){
            return GivenMembership.ALREADY_REGISTER_NOT_INVEST_1000;
        }

        if(registerTime != null && DateTime.parse(heroRankingActivityPeriod.get(0),DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate().after(registerTime) && investAmount >= 1000){
            createUserMembershipModel(loginName, MembershipLevel.V5.getLevel());
            return GivenMembership.ALREADY_REGISTER_ALREADY_INVEST_1000;
        }

        createUserMembershipModel(loginName, MembershipLevel.V5.getLevel());
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
