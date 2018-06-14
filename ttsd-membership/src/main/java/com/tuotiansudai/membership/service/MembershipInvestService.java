package com.tuotiansudai.membership.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.dto.SmsNotifyDto;
import com.tuotiansudai.enums.*;
import com.tuotiansudai.membership.repository.mapper.MembershipExperienceBillMapper;
import com.tuotiansudai.membership.repository.mapper.MembershipMapper;
import com.tuotiansudai.membership.repository.mapper.UserMembershipMapper;
import com.tuotiansudai.membership.repository.model.MembershipExperienceBillModel;
import com.tuotiansudai.membership.repository.model.MembershipModel;
import com.tuotiansudai.membership.repository.model.UserMembershipModel;
import com.tuotiansudai.message.EventMessage;
import com.tuotiansudai.message.PushMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.rest.client.mapper.UserMapper;
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

    @Autowired
    private UserMapper userMapper;

    @Transactional
    public void afterInvestSuccess(String loginName, long investAmount, long investId, String loanName) {
        try {
            if (membershipExperienceBillMapper.findByLoginNameAndInvestId(loginName, investId) != null) {
                // 检查是否已经处理过，幂等操作
                logger.info(MessageFormat.format(
                        "membership point has been processed already, won't do it again. loginName:{0}, investId:{1}", loginName, String.valueOf(investId)));
                return;
            }

            AccountModel accountModel = accountMapper.lockByLoginName(loginName);
            long investMembershipPoint = investAmount / 100;
            accountModel.setMembershipPoint(accountModel.getMembershipPoint() + investMembershipPoint);
            accountMapper.update(accountModel);

            MembershipExperienceBillModel billModel = new MembershipExperienceBillModel(loginName,
                    String.valueOf(investId),
                    investMembershipPoint,
                    accountModel.getMembershipPoint(),
                    MessageFormat.format("您投资了{0}项目{1}元", loanName, AmountConverter.convertCentToString(investAmount)));

            membershipExperienceBillMapper.create(billModel);

            int level = userMembershipEvaluator.evaluateUpgradeLevel(loginName).getLevel();
            MembershipModel newMembership = membershipMapper.findByExperience(accountModel.getMembershipPoint());
            if (newMembership.getLevel() > level) {
                UserMembershipModel curUserMembershipModel = userMembershipMapper.findCurrentUpgradeMaxByLoginName(loginName);
                curUserMembershipModel.setExpiredTime(new Date());
                userMembershipMapper.update(curUserMembershipModel);

                UserMembershipModel newUserMembershipModel = UserMembershipModel.createUpgradeUserMembershipModel(loginName, newMembership.getId());
                userMembershipMapper.create(newUserMembershipModel);
                this.sendMessage(loginName, newMembership.getLevel());
            }
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            throw e;
        }
    }

    private void sendMessage(String loginName, int level) {
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

        mqWrapperClient.sendMessage(MessageQueue.SmsNotify, new SmsNotifyDto(JianZhouSmsTemplate.SMS_MEMBERSHIP_UPGRADE_TEMPLATE, Lists.newArrayList(userMapper.findByLoginName(loginName).getMobile()), Lists.newArrayList(String.valueOf(level), String.valueOf(level))));
    }
}
