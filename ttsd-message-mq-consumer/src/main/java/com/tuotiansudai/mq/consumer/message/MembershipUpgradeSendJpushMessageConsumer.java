package com.tuotiansudai.mq.consumer.message;

import com.tuotiansudai.message.util.UserMessageEventGenerator;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Component
public class MembershipUpgradeSendJpushMessageConsumer implements MessageConsumer {
    private static Logger logger = LoggerFactory.getLogger(MembershipUpgradeSendJpushMessageConsumer.class);

    @Autowired
    private UserMessageEventGenerator userMessageEventGenerator;

    @Override
    public MessageQueue queue() {
        return MessageQueue.MembershipUpgrade_SendJpushMessage;
    }

    @Transactional
    @Override
    public void consume(String message) {

        logger.info("[MQ] receive message: {}: {}.", this.queue(), message);
        if (!StringUtils.isEmpty(message)) {
            String args[] = message.split(":");
            if (args.length == 2) {
                String loginName = args[0];
                String membershipId = args[1];
                logger.info("[MQ] ready to consume message: MembershipUpgrade_SendJpushMessage. loginName:{}, membershipId:{}", loginName, membershipId);
                userMessageEventGenerator.generateMembershipUpgradeEvent(loginName, Long.parseLong(membershipId));
                logger.info("[MQ] consume message success.");
            }
        }
    }
}