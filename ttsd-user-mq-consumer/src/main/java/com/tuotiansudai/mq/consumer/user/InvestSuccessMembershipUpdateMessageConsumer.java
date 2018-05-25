package com.tuotiansudai.mq.consumer.user;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.tuotiansudai.fudian.message.BankInvestMessage;
import com.tuotiansudai.membership.service.MembershipInvestService;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InvestSuccessMembershipUpdateMessageConsumer implements MessageConsumer {
    private static Logger logger = LoggerFactory.getLogger(InvestSuccessMembershipUpdateMessageConsumer.class);

    private final MembershipInvestService membershipInvestService;

    @Autowired
    public InvestSuccessMembershipUpdateMessageConsumer(MembershipInvestService membershipInvestService) {
        this.membershipInvestService = membershipInvestService;
    }

    @Override
    public MessageQueue queue() {
        return MessageQueue.Invest_MembershipUpdate;
    }

    @Override
    public void consume(String message) {
        logger.info("[MQ] receive message: {}: {}.", this.queue(), message);

        if (Strings.isNullOrEmpty(message)) {
            logger.info("[MQ] receive message is empty");
            return;
        }

        try {
            BankInvestMessage bankInvestMessage = new Gson().fromJson(message, BankInvestMessage.class);
            membershipInvestService.afterInvestSuccess(bankInvestMessage.getLoginName(),
                    bankInvestMessage.getAmount(),
                    bankInvestMessage.getInvestId(),
                    bankInvestMessage.getLoanName());
        } catch (JsonSyntaxException e) {
            logger.error("[MQ] parse message failed: {}: '{}'.", this.queue(), message);
        }
    }
}