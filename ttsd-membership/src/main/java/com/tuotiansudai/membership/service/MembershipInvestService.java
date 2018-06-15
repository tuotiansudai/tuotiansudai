package com.tuotiansudai.membership.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.enums.AppUrl;
import com.tuotiansudai.enums.MessageEventType;
import com.tuotiansudai.enums.PushSource;
import com.tuotiansudai.enums.PushType;
import com.tuotiansudai.fudian.message.BankLoanInvestMessage;
import com.tuotiansudai.membership.repository.mapper.MembershipExperienceBillMapper;
import com.tuotiansudai.membership.repository.mapper.MembershipMapper;
import com.tuotiansudai.membership.repository.mapper.UserMembershipMapper;
import com.tuotiansudai.membership.repository.model.MembershipExperienceBillModel;
import com.tuotiansudai.membership.repository.model.MembershipModel;
import com.tuotiansudai.membership.repository.model.UserMembershipModel;
import com.tuotiansudai.message.EventMessage;
import com.tuotiansudai.message.PushMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.repository.mapper.BankAccountMapper;
import com.tuotiansudai.repository.model.BankAccountModel;
import com.tuotiansudai.util.AmountConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Date;

@Service
public class MembershipInvestService {

    private static Logger logger = LoggerFactory.getLogger(MembershipInvestService.class);

    private final MembershipExperienceBillMapper membershipExperienceBillMapper;

    private final BankAccountMapper bankAccountMapper;

    private final UserMembershipMapper userMembershipMapper;

    private final MembershipMapper membershipMapper;

    private final UserMembershipEvaluator userMembershipEvaluator;

    private final MQWrapperClient mqWrapperClient;

    @Autowired
    public MembershipInvestService(MembershipExperienceBillMapper membershipExperienceBillMapper, BankAccountMapper bankAccountMapper, UserMembershipMapper userMembershipMapper, MembershipMapper membershipMapper, UserMembershipEvaluator userMembershipEvaluator, MQWrapperClient mqWrapperClient) {
        this.membershipExperienceBillMapper = membershipExperienceBillMapper;
        this.bankAccountMapper = bankAccountMapper;
        this.userMembershipMapper = userMembershipMapper;
        this.membershipMapper = membershipMapper;
        this.userMembershipEvaluator = userMembershipEvaluator;
        this.mqWrapperClient = mqWrapperClient;
    }

    @Transactional(rollbackFor = Exception.class)
    public void afterInvestSuccess(BankLoanInvestMessage bankLoanInvestMessage) {
        String loginName = bankLoanInvestMessage.getLoginName();
        long investId = bankLoanInvestMessage.getInvestId();
        long amount = bankLoanInvestMessage.getAmount();
        if (membershipExperienceBillMapper.findByLoginNameAndInvestId(loginName, investId) != null) {
            // 检查是否已经处理过，幂等操作
            logger.warn("membership point has been processed already, won't do it again. message: {}", bankLoanInvestMessage);
            return;
        }


        long investMembershipPoint = amount / 100;
        bankAccountMapper.updateMembershipPoint(loginName, investMembershipPoint);
        BankAccountModel bankAccountModel = bankAccountMapper.findByLoginName(loginName);
        membershipExperienceBillMapper.create(new MembershipExperienceBillModel(loginName,
                String.valueOf(investId),
                investMembershipPoint,
                bankAccountModel.getMembershipPoint(),
                MessageFormat.format("您投资了{0}项目{1}元", bankLoanInvestMessage.getLoanName(), AmountConverter.convertCentToString(amount))));

        int level = userMembershipEvaluator.evaluateUpgradeLevel(loginName).getLevel();

        MembershipModel newMembership = membershipMapper.findByExperience(bankAccountModel.getMembershipPoint());
        if (newMembership.getLevel() > level) {
            UserMembershipModel curUserMembershipModel = userMembershipMapper.findCurrentUpgradeMaxByLoginName(loginName);
            curUserMembershipModel.setExpiredTime(new Date());
            userMembershipMapper.update(curUserMembershipModel);
            UserMembershipModel newUserMembershipModel = UserMembershipModel.createUpgradeUserMembershipModel(loginName, newMembership.getId());
            userMembershipMapper.create(newUserMembershipModel);
            this.sendMessage(loginName, newMembership.getLevel());
        }
    }

    private void sendMessage(String loginName, int level) {
        try {
            //Title:恭喜您会员等级提升至V{0}
            //Content:尊敬的用户，恭喜您会员等级提升至V{0}，拓天速贷为您准备了更多会员特权，快来查看吧。
            String title = MessageFormat.format(MessageEventType.MEMBERSHIP_UPGRADE.getTitleTemplate(), String.valueOf(level));
            String content = MessageFormat.format(MessageEventType.MEMBERSHIP_UPGRADE.getContentTemplate(), String.valueOf(level));
            mqWrapperClient.sendMessage(MessageQueue.EventMessage, new EventMessage(MessageEventType.MEMBERSHIP_UPGRADE,
                    Lists.newArrayList(loginName),
                    title,
                    content,
                    null
            ));
            mqWrapperClient.sendMessage(MessageQueue.PushMessage, new PushMessage(Lists.newArrayList(loginName), PushSource.ALL, PushType.MEMBERSHIP_UPGRADE, title, AppUrl.MESSAGE_CENTER_LIST));
        } catch (Exception ex) {
            logger.error(ex.getLocalizedMessage(), ex);
        }

    }
}
