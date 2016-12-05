package com.tuotiansudai.mq.consumer.user;

import com.tuotiansudai.membership.service.MembershipInvestService;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.service.InvestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Component
public class InvestSuccessMembershipUpdateMessageConsumer implements MessageConsumer {
    private static Logger logger = LoggerFactory.getLogger(InvestSuccessMembershipUpdateMessageConsumer.class);

    @Autowired
    private MembershipInvestService membershipInvestService;

    @Autowired
    private InvestService investService;

    @Override
    public MessageQueue queue() {
        return MessageQueue.InvestSuccess_MembershipUpdate;
    }

    @Transactional
    @Override
    public void consume(String message) {

        logger.info("[MQ] receive message: {}: {}.", this.queue(), message);
        if (!StringUtils.isEmpty(message)) {
            InvestModel investModel = investService.findById(Long.parseLong(message));
            String loginName = investModel.getLoginName();
            long investId = investModel.getId();
            long amount = investModel.getAmount();

            logger.info("[MQ] ready to consume message: InvestSuccess-MembershipUpdate. loginName:{}, investId:{}", loginName, investId);

            membershipInvestService.afterInvestSuccess(loginName, amount, investId);
        }
        logger.info("[MQ] consume message success.");
    }
}
}
