package com.tuotiansudai.scheduler.user;

import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.enums.MessageEventType;
import com.tuotiansudai.enums.PushSource;
import com.tuotiansudai.enums.PushType;
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
    private UserMembershipMapper userMembershipMapper;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Scheduled(cron = "0 0 10 * * ?", zone = "Asia/Shanghai")
    public void membershipExpiredMessage() {
        List<String> membershipExpiredUsers = userMembershipMapper.findLevelFiveMembershipExpiredUsers();
        if (CollectionUtils.isEmpty(membershipExpiredUsers)) {
            logger.info("[EventMessageJob] today is no user whose membership is expired");
            return;
        }
        //Title:您的V5会员已到期，请前去购买
        //Content:尊敬的用户，您的V5会员已到期，V5会员可享受服务费7折优惠，平台也将会在V5会员生日时送上神秘礼包哦。请及时续费以免耽误您获得投资奖励！
        String title = MessageEventType.MEMBERSHIP_EXPIRED.getTitleTemplate();
        String content = MessageEventType.MEMBERSHIP_EXPIRED.getContentTemplate();
        mqWrapperClient.sendMessage(MessageQueue.EventMessage, new EventMessage(MessageEventType.MEMBERSHIP_EXPIRED, membershipExpiredUsers, title, content, null));
        mqWrapperClient.sendMessage(MessageQueue.PushMessage, new PushMessage(membershipExpiredUsers, PushSource.ALL, PushType.MEMBERSHIP_EXPIRED, title));
    }
}
