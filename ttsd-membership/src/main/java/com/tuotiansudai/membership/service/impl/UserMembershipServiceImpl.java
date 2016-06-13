package com.tuotiansudai.membership.service.impl;

import com.tuotiansudai.membership.repository.mapper.MembershipMapper;
import com.tuotiansudai.membership.repository.mapper.UserMembershipMapper;
import com.tuotiansudai.membership.repository.model.MembershipModel;
import com.tuotiansudai.membership.repository.model.MembershipType;
import com.tuotiansudai.membership.repository.model.UserMembershipModel;
import com.tuotiansudai.membership.service.UserMembershipEvaluator;
import com.tuotiansudai.membership.service.UserMembershipService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.model.AccountModel;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
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

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private AccountMapper accountMapper;

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
    public MembershipType receiveMembership(String loanName){
        setMembershipStartDate();
        if(Calendar.getInstance().getTime().getTime() < membershipStartDate.getTime()){
            return MembershipType.NOT_TO_THE_TIME;
        }

        if(loanName == null || loanName.equals("")){
            return MembershipType.NOT_TO_LOGIN;
        }

        AccountModel accountModel = accountMapper.findByLoginName(loanName);
        if(accountModel == null || StringUtils.isEmpty(accountModel.getIdentityNumber())){
            return MembershipType.NOT_TO_REGISTER;
        }

        if(CollectionUtils.isNotEmpty(userMembershipMapper.findByLoginName(loanName))){
            return MembershipType.ALREADY_RECEIVE;
        }

        long investAmount = investMapper.sumSuccessInvestAmountByLoginName(null,loanName);
        if(accountModel.getRegisterTime().getTime() < membershipStartDate.getTime() && investAmount < 1000){
            return MembershipType.ALREADY_REGISTER_NOT_INVEST_1000;
        }

        if(accountModel.getRegisterTime().getTime() < membershipStartDate.getTime() && investAmount >= 1000){
            return MembershipType.ALREADY_REGISTER_ALREADY_INVEST_1000;
        }

        if(accountModel.getRegisterTime().getTime() >= membershipStartDate.getTime()){
            return MembershipType.AFTER_START_ACTIVITY_REGISTER;
        }

        return null;
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
}
