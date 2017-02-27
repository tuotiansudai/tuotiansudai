package com.tuotiansudai.mq.consumer.user;

import com.tuotiansudai.enums.ExperienceBillBusinessType;
import com.tuotiansudai.enums.ExperienceBillOperationType;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.service.ExperienceInvestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Component
public class RegisterSuccessExperienceBalanceUpdateMessageConsumer implements MessageConsumer {
    private static Logger logger = LoggerFactory.getLogger(RegisterSuccessExperienceBalanceUpdateMessageConsumer.class);

    private static final long EXPERIENCE_AMOUNT = 688800;

    @Autowired
    private ExperienceInvestService experienceInvestService;

    @Override
    public MessageQueue queue() {
        return MessageQueue.UserRegistered_CompleteExperienceUpdate;
    }

    @Transactional
    @Override
    public void consume(String message) {

        logger.info("[MQ] receive message: {}: {}.", this.queue(), message);
        if (!StringUtils.isEmpty(message)) {
            experienceInvestService.updateUserExperienceBalanceByLoginName(EXPERIENCE_AMOUNT, message, ExperienceBillOperationType.IN, ExperienceBillBusinessType.REGISTER);
        }
        logger.info("[MQ] consume message success.");
    }
}