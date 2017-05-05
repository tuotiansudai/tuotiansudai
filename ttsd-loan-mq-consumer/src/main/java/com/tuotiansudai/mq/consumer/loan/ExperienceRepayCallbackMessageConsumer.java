package com.tuotiansudai.mq.consumer.loan;

import com.google.common.base.Strings;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.Environment;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ExperienceRepayCallbackMessageConsumer implements MessageConsumer {

    private static Logger logger = LoggerFactory.getLogger(ExperienceRepayCallbackMessageConsumer.class);

    private final PayWrapperClient payWrapperClient;

    @Value("common.environment")
    private Environment env;

    @Autowired
    public ExperienceRepayCallbackMessageConsumer(PayWrapperClient payWrapperClient) {
        this.payWrapperClient = payWrapperClient;
    }

    @Override
    public MessageQueue queue() {
        return MessageQueue.ExperienceRepayCallback;
    }

    @Override
    public void consume(String message) {
        logger.info("[ExperienceRepayCallbackMessageConsumer] receive message: {}: {}.", this.queue(), message);

        if (!Environment.isProduction(env)) {
            logger.info("pause send experience repay in QA/DEV environment.");
            return;
        }

        if (Strings.isNullOrEmpty(message)) {
            logger.warn("[ExperienceRepayCallbackMessageConsumer] receive message is empty");
            return;
        }

        logger.info("[MQ] experience interest callback ready to consume message");

        BaseDto<PayDataDto> result = payWrapperClient.postExperienceRepay(Long.parseLong(message));
        if (!result.isSuccess()) {
            logger.error("experience interest callback consume fail. notifyRequestId: " + message);
            throw new RuntimeException("experience interest callback consume fail. notifyRequestId: " + message);
        }
        logger.info("[MQ] experience interest callback consume message success.");
    }
}
