package com.tuotiansudai.membership.service;

import com.tuotiansudai.membership.repository.mapper.MembershipExperienceBillMapper;
import com.tuotiansudai.membership.repository.mapper.MembershipMapper;
import com.tuotiansudai.membership.repository.mapper.UserMembershipMapper;
import com.tuotiansudai.membership.repository.model.MembershipExperienceBillModel;
import com.tuotiansudai.membership.repository.model.MembershipModel;
import com.tuotiansudai.membership.repository.model.UserMembershipModel;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.util.AmountConverter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Service
public class MembershipInvestService {

    private static Logger logger = Logger.getLogger(MembershipInvestService.class);

    @Autowired
    private MembershipExperienceBillMapper membershipExperienceBillMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private UserMembershipMapper userMembershipMapper;

    @Autowired
    private MembershipMapper membershipMapper;

    @Autowired
    private UserMembershipEvaluator userMembershipEvaluator;

    public void afterInvestSuccess(String loginName, long investAmount, long investId) {
        try {
            AccountModel accountModel = accountMapper.findByLoginName(loginName);
            long investMembershipPoint = investAmount / 100;
            accountModel.setMembershipPoint(accountModel.getMembershipPoint() + investMembershipPoint);
            accountMapper.update(accountModel);

            MembershipExperienceBillModel billModel = new MembershipExperienceBillModel(loginName,
                    investMembershipPoint,
                    accountModel.getMembershipPoint(),
                    MessageFormat.format("您投资了{0}项目{1}元", String.valueOf(investId), AmountConverter.convertCentToString(investAmount)));

            membershipExperienceBillMapper.create(billModel);

            int level = userMembershipEvaluator.evaluateUpgradeLevel(loginName).getLevel();
            MembershipModel newMembership = membershipMapper.findByExperience(accountModel.getMembershipPoint());
            if (newMembership.getLevel() > level) {
                UserMembershipModel userMembershipModel = UserMembershipModel.createUpgradeUserMembershipModel(loginName, newMembership.getId());
                userMembershipMapper.create(userMembershipModel);
            }
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }
}
