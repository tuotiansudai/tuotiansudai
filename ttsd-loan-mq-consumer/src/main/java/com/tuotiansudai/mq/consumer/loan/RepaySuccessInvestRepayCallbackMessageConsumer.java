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
public class RepaySuccessInvestRepayCallbackMessageConsumer implements MessageConsumer {
    private static Logger logger = LoggerFactory.getLogger(RepaySuccessInvestRepayCallbackMessageConsumer.class);

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Override
    public MessageQueue queue() {
        return MessageQueue.RepaySuccessInvestRepayCallback;
    }

    @Transactional
    @Override
    public void consume(String message) {
        logger.info("[还款发放投资人收益回调MQ] receive message: {}: {}.", this.queue(), message);

        if (!StringUtils.isEmpty(message)) {
            logger.info("[还款发放投资人收益回调MQ] ready to consume message: repay callback.");
            BaseDto<PayDataDto> result = payWrapperClient.advanceRepayInvestPayback(Long.parseLong(message));
            if (!result.isSuccess()) {
                logger.error("invest repay callback consume fail. notifyRequestId: " + message);
                throw new RuntimeException("invest repay callback consume fail. notifyRequestId: " + message);
            }
            logger.info("[还款发放投资人收益回调MQ] consume message success.");
        }
    }
}
