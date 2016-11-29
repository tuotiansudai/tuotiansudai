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

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Override
    public MessageQueue queue() {
        return MessageQueue.InvestCallback;
    }

    @Transactional
    @Override
    public void consume(String message) {
        logger.info("[MQ] receive message: {}: {}.", this.queue(), message);
        if (!StringUtils.isEmpty(message)) {
            String investNotifyRequestId = message;
            logger.info("[MQ] ready to consumer message: invest callback.");
            BaseDto<PayDataDto> result = payWrapperClient.investCallback(investNotifyRequestId);
            if (!result.isSuccess()) {
                logger.error("invest callback consume fail. investNotifyRequestId: " + investNotifyRequestId);
                throw new RuntimeException("invest callback consume fail. investNotifyRequestId: " + investNotifyRequestId);
            }
            logger.info("[MQ] consumer message done.");

//                logger.info("[MQ] invest callback success, begin publish message.");
//                mqClient.publishMessage(MessageTopic.InvestSuccess, "");
        }
    }
}
