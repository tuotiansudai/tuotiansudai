package com.tuotiansudai.scheduler.user;

import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.enums.MessageEventType;
import com.tuotiansudai.enums.PushSource;
import com.tuotiansudai.enums.PushType;
import com.tuotiansudai.membership.repository.mapper.MembershipPrivilegeMapper;
import com.tuotiansudai.membership.repository.mapper.UserMembershipMapper;
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

@Component
public class MembershipExpiredNotifyScheduler {

    private static Logger logger = LoggerFactory.getLogger(MembershipExpiredNotifyScheduler.class);

    @Autowired
    private MembershipPrivilegeMapper membershipPrivilegeMapper;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Scheduled(cron = "0 0 10 * * ?", zone = "Asia/Shanghai")
    public void membershipExpiredMessage() {
        List<String> membershipPrivilegeExpiredUsers = membershipPrivilegeMapper.findMembershipPrivilegeExpiredUsers();
        if (CollectionUtils.isEmpty(membershipPrivilegeExpiredUsers)) {
            logger.info("[EventMessageJob] today is no user whose membership privilege is expired");
            return;
        }
        //Title:您购买的增值特权已过期
        //Content:尊敬的用户，您购买的增值特权已过期，增值特权可享受服务费7折优惠，请及时续费。
        String title = MessageEventType.MEMBERSHIP_PRIVILEGE_EXPIRED.getTitleTemplate();
        String content = MessageEventType.MEMBERSHIP_PRIVILEGE_EXPIRED.getContentTemplate();
        mqWrapperClient.sendMessage(MessageQueue.EventMessage, new EventMessage(MessageEventType.MEMBERSHIP_PRIVILEGE_EXPIRED, membershipPrivilegeExpiredUsers, title, content, null));
        mqWrapperClient.sendMessage(MessageQueue.PushMessage, new PushMessage(membershipPrivilegeExpiredUsers, PushSource.ALL, PushType.MEMBERSHIP_EXPIRED, title));
    }
}
