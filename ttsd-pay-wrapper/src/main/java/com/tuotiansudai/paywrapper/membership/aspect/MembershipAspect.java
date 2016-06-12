package com.tuotiansudai.paywrapper.membership.aspect;

import com.tuotiansudai.membership.repository.mapper.MembershipExperienceBillMapper;
import com.tuotiansudai.membership.repository.mapper.MembershipMapper;
import com.tuotiansudai.membership.repository.mapper.UserMembershipMapper;
import com.tuotiansudai.membership.repository.model.MembershipExperienceBillModel;
import com.tuotiansudai.membership.repository.model.MembershipModel;
import com.tuotiansudai.membership.repository.model.UserMembershipModel;
import com.tuotiansudai.membership.repository.model.UserMembershipType;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.InvestModel;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Aspect
public class MembershipAspect {

    private static Logger logger = Logger.getLogger(MembershipAspect.class);

    @Autowired
    AccountMapper accountMapper;

    @Autowired
    MembershipExperienceBillMapper membershipExperienceBillMapper;

    @Autowired
    UserMembershipMapper userMembershipMapper;

    @Autowired
    MembershipMapper membershipMapper;

    /**
     * after invest success, add membership_point, create membership_experience_bill, upgrade if possible
     *
     * @param joinPoint
     */
    @After(value = "execution(* com.tuotiansudai.paywrapper.service.InvestService.investSuccess(..))")
    public void afterReturningInvestSuccess(JoinPoint joinPoint) {
        InvestModel investModel = (InvestModel) joinPoint.getArgs()[0];
        logger.debug("into MembershipAspect afterReturningInvestSuccess, loginName: " + investModel.getLoginName());
        try {
            String loginName = investModel.getLoginName();
            AccountModel accountModel = accountMapper.findByLoginName(loginName);
            long membershipPoint = accountModel.getMembershipPoint();
            long investAmount = investModel.getAmount() / 100;
            long totalPoint = membershipPoint + investAmount;
            accountModel.setMembershipPoint(totalPoint);
            accountMapper.update(accountModel);

            MembershipExperienceBillModel billModel = new MembershipExperienceBillModel(loginName, investAmount, totalPoint, new Date(), "UPGRADE");
            membershipExperienceBillMapper.create(billModel);

            Integer level = userMembershipMapper.findRealLevelByLoginName(loginName);
            MembershipModel newMembership = membershipMapper.findByExperience(totalPoint);
            if (newMembership.getLevel() > level) {
                UserMembershipModel userMembershipModel = new UserMembershipModel(loginName, newMembership.getId(), new DateTime(2200, 1, 1, 1, 1).toDate(), UserMembershipType.UPGRADE);
                userMembershipMapper.create(userMembershipModel);
            }
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        logger.debug("left MembershipAspect afterReturningInvestSuccess, loginName: " + investModel.getLoginName());
    }


}
