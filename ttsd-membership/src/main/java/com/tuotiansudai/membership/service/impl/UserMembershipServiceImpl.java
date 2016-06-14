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
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    private static Date membershipStartDate = null;

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
    public MembershipType receiveMembership(String loginName){
        setMembershipStartDate();
        if(Calendar.getInstance().getTime().getTime() < membershipStartDate.getTime()){
            return MembershipType.NOT_TO_THE_TIME;
        }

        if(loginName == null || loginName.equals("")){
            return MembershipType.NOT_TO_LOGIN;
        }

        String identityNumber = userMembershipMapper.findAccountIdentityNumberByLoginName(loginName);
        if(identityNumber == null || identityNumber.equals("0")){
            return MembershipType.NOT_TO_REGISTER;
        }

        if(CollectionUtils.isNotEmpty(userMembershipMapper.findByLoginName(loginName))){
            return MembershipType.ALREADY_RECEIVE;
        }

        long investAmount = userMembershipMapper.sumSuccessInvestAmountByLoginName(loginName);
        Date registerTime = userMembershipMapper.findAccountRegisterTimeByLoginName(loginName);
        if(registerTime != null && registerTime.getTime() < membershipStartDate.getTime() && investAmount < 1000){
            return MembershipType.ALREADY_REGISTER_NOT_INVEST_1000;
        }

        if(registerTime != null && registerTime.getTime() < membershipStartDate.getTime() && investAmount >= 1000){
            createUserMembershipModel(loginName, MembershipLevel.V5.getLevel());
            return MembershipType.ALREADY_REGISTER_ALREADY_INVEST_1000;
        }

        return MembershipType.AFTER_START_ACTIVITY_REGISTER;
    }

    private void setMembershipStartDate(){
        if(membershipStartDate == null){
            try {
                membershipStartDate = sdf.parse("2016-07-01");
            } catch (ParseException e) {
                logger.error(e.getLocalizedMessage(), e);
            }
        }
    }

    private void createUserMembershipModel(String loginName,int level){
        UserMembershipModel userMembershipModel = new UserMembershipModel();
        userMembershipModel.setLoginName(loginName);
        userMembershipModel.setCreatedTime(new Date());
        Calendar lastDate = Calendar.getInstance();
        lastDate.add(Calendar.MONTH, +1);
        userMembershipModel.setExpiredTime(lastDate.getTime());
        userMembershipModel.setType(UserMembershipType.GIVEN);
        userMembershipModel.setMembershipId(membershipMapper.findByLevel(level).getId());
        userMembershipMapper.create(userMembershipModel);
    }
}
