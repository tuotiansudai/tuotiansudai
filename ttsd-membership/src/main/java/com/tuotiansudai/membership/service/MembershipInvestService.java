package com.tuotiansudai.membership.service;

import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.membership.repository.mapper.MembershipExperienceBillMapper;
import com.tuotiansudai.membership.repository.mapper.MembershipMapper;
import com.tuotiansudai.membership.repository.mapper.UserMembershipMapper;
import com.tuotiansudai.membership.repository.model.MembershipExperienceBillModel;
import com.tuotiansudai.membership.repository.model.MembershipModel;
import com.tuotiansudai.membership.repository.model.UserMembershipModel;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.util.AmountConverter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Date;

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

    @Autowired
    private MQWrapperClient mqWrapperClient;


    @Transactional
    public void afterInvestSuccess(String loginName, long investAmount, long investId) {
        try {
            if (membershipExperienceBillMapper.findByLoginNameAndInvestId(loginName, investId) != null) {
                // 检查是否已经处理过，幂等操作
                logger.info(MessageFormat.format(
                        "membership point has been processed already, won't do it again. loginName:{0}, investId:{1}", loginName, String.valueOf(investId)));
                return;
            }

            AccountModel accountModel = accountMapper.findByLoginName(loginName);
            long investMembershipPoint = investAmount / 100;
            accountModel.setMembershipPoint(accountModel.getMembershipPoint() + investMembershipPoint);
            accountMapper.update(accountModel);

            MembershipExperienceBillModel billModel = new MembershipExperienceBillModel(loginName,
                    String.valueOf(investId),
                    investMembershipPoint,
                    accountModel.getMembershipPoint(),
                    MessageFormat.format("您投资了{0}项目{1}元", String.valueOf(investId), AmountConverter.convertCentToString(investAmount)));

            membershipExperienceBillMapper.create(billModel);

            int level = userMembershipEvaluator.evaluateUpgradeLevel(loginName).getLevel();
            MembershipModel newMembership = membershipMapper.findByExperience(accountModel.getMembershipPoint());
            if (newMembership.getLevel() > level) {
                logger.info(MessageFormat.format("will upgrade user membership level, loginName:{0}, investId:{1}, newLevel:{2}",
                        loginName, String.valueOf(investId),newMembership.getLevel()));

                UserMembershipModel curUserMembershipModel = userMembershipMapper.findCurrentUpgradeMaxByLoginName(loginName);
                curUserMembershipModel.setExpiredTime(new Date());
                userMembershipMapper.update(curUserMembershipModel);

                UserMembershipModel newUserMembershipModel = UserMembershipModel.createUpgradeUserMembershipModel(loginName, newMembership.getId());
                userMembershipMapper.create(newUserMembershipModel);

                mqWrapperClient.sendMessage(MessageQueue.MembershipUpgrade_SendJpushMessage, loginName + ":" + String.valueOf(newMembership.getId()));
            }
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            throw e;
        }
    }
}
