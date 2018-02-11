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
public class InvestCallbackMessageConsumer implements MessageConsumer {
    private static Logger logger = LoggerFactory.getLogger(InvestCallbackMessageConsumer.class);

    private final PayWrapperClient payWrapperClient;

    @Autowired
    public InvestCallbackMessageConsumer(PayWrapperClient payWrapperClient) {
        this.payWrapperClient = payWrapperClient;
    }

    @Override
    public MessageQueue queue() {
        return MessageQueue.InvestCallback;
    }

    @Transactional
    @Override
    public void consume(String message) {
        logger.info("[MQ] receive message: {}: {}.", this.queue(), message);

        if (!StringUtils.isEmpty(message)) {
            logger.info("[MQ] ready to consume message: invest callback.");

            BaseDto<PayDataDto> result = payWrapperClient.investCallback(message);

            if (!result.isSuccess()) {
                logger.error("invest callback consume fail. investId: " + message);
                throw new RuntimeException("invest callback consume fail. investId: " + message);
            }

            logger.info("[MQ] consume message success.");
        }
    }
}
