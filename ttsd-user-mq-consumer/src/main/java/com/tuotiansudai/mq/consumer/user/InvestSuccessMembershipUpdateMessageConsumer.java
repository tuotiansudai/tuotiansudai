package com.tuotiansudai.mq.consumer.user;

import com.tuotiansudai.membership.service.MembershipInvestService;
import com.tuotiansudai.message.InvestSuccessMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.util.JsonConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Component
public class InvestSuccessMembershipUpdateMessageConsumer implements MessageConsumer {
    private static Logger logger = LoggerFactory.getLogger(InvestSuccessMembershipUpdateMessageConsumer.class);

    @Autowired
    private MembershipInvestService membershipInvestService;

    @Autowired
    private LoanMapper loanMapper;

    @Override
    public MessageQueue queue() {
        return MessageQueue.InvestSuccess_MembershipUpdate;
    }

    @Transactional
    @Override
    public void consume(String message) {

        logger.info("[MQ] receive message: {}: {}.", this.queue(), message);
        if (!StringUtils.isEmpty(message)) {
            InvestSuccessMessage investSuccessMessage;
            try {
                investSuccessMessage = JsonConverter.readValue(message, InvestSuccessMessage.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            String loginName = investSuccessMessage.getInvestInfo().getLoginName();
            long investId = investSuccessMessage.getInvestInfo().getInvestId();
            long amount = investSuccessMessage.getInvestInfo().getAmount();
            long loanId = investSuccessMessage.getLoanDetailInfo().getLoanId();
            LoanModel loanModel = loanMapper.findById(loanId);

            logger.info("[MQ] ready to consume message: InvestSuccess-MembershipUpdate. loginName:{}, investId:{}", loginName, investId);

            membershipInvestService.afterInvestSuccess(loginName, amount, investId, loanModel.getName());
        }
        logger.info("[MQ] consume message success.");
    }
}