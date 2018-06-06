package com.tuotiansudai.scheduler.user;

import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.dto.SmsNotifyDto;
import com.tuotiansudai.enums.*;
import com.tuotiansudai.membership.repository.mapper.MembershipPrivilegeMapper;
import com.tuotiansudai.membership.repository.model.MembershipPrivilegeExpiredUsersView;
import com.tuotiansudai.message.EventMessage;
import com.tuotiansudai.message.PushMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MembershipExpiredNotifyScheduler {

    private static Logger logger = LoggerFactory.getLogger(MembershipExpiredNotifyScheduler.class);

    @Autowired
    private MembershipPrivilegeMapper membershipPrivilegeMapper;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Scheduled(cron = "0 0 10 * * ?", zone = "Asia/Shanghai")
    public void membershipExpiredMessage() {
        List<MembershipPrivilegeExpiredUsersView> membershipPrivilegeExpiredUsers = membershipPrivilegeMapper.findMembershipPrivilegeExpiredUsers();
        if (CollectionUtils.isEmpty(membershipPrivilegeExpiredUsers)) {
            logger.info("[EventMessageJob] today is no user whose membership privilege is expired");
            return;
        }
        //Title:您购买的增值特权已过期
        //Content:尊敬的用户，您购买的增值特权已过期，增值特权可享受服务费7折优惠，请及时续费。
        String title = MessageEventType.MEMBERSHIP_PRIVILEGE_EXPIRED.getTitleTemplate();
        String content = MessageEventType.MEMBERSHIP_PRIVILEGE_EXPIRED.getContentTemplate();
        List<String> loginNames = membershipPrivilegeExpiredUsers.stream().map(MembershipPrivilegeExpiredUsersView::getLoginName).collect(Collectors.toList());
        mqWrapperClient.sendMessage(MessageQueue.EventMessage, new EventMessage(MessageEventType.MEMBERSHIP_PRIVILEGE_EXPIRED, loginNames, title, content, null));
        mqWrapperClient.sendMessage(MessageQueue.PushMessage, new PushMessage(loginNames, PushSource.ALL, PushType.MEMBERSHIP_PRIVILEGE_EXPIRED, title, AppUrl.MESSAGE_CENTER_LIST));

        List<String> mobiles = membershipPrivilegeExpiredUsers.stream().map(MembershipPrivilegeExpiredUsersView::getMobile).collect(Collectors.toList());
        mqWrapperClient.sendMessage(MessageQueue.SmsNotify, new SmsNotifyDto(JianZhouSmsTemplate.SMS_MEMBERSHIP_PRIVILEGE_EXPIRED_TEMPLATE, mobiles));
    }
}
