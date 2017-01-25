package com.tuotiansudai.mq.consumer.loan;

import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Component
public class ExperienceInterestCallbackMessageConsumer implements MessageConsumer {
    private static Logger logger = LoggerFactory.getLogger(ExperienceInterestCallbackMessageConsumer.class);

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Override
    public MessageQueue queue() {
        return MessageQueue.ExperienceInterestCallback;
    }

    @Transactional
    @Override
    public void consume(String message) {
        logger.info("[MQ] receive message: {}: {}.", this.queue(), message);
        if (!StringUtils.isEmpty(message)) {
            logger.info("[MQ] experience interest callback ready to consume message");
            BaseDto<PayDataDto> result = payWrapperClient.experienceInterestCallback(message);
            if (!result.isSuccess()) {
                logger.error("experience interest callback consume fail. notifyRequestId: " + message);
                throw new RuntimeException("experience interest callback consume fail. notifyRequestId: " + message);
            }
            logger.info("[MQ] experience interest callback consume message success.");
        }
    }
}
