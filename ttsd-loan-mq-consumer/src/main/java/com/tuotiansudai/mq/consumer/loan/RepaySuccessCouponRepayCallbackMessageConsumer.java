package com.tuotiansudai.mq.consumer.loan;

import com.google.common.base.Strings;
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

@Component
public class RepaySuccessCouponRepayCallbackMessageConsumer implements MessageConsumer {
    private static Logger logger = LoggerFactory.getLogger(RepaySuccessCouponRepayCallbackMessageConsumer.class);

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Override
    public MessageQueue queue() {
        return MessageQueue.RepaySuccessCouponRepayCallback;
    }

    @Transactional
    @Override
    public void consume(String message) {
        logger.info("[还款优惠券收益回调MQ] receive message: {}: {}.", this.queue(), message);
        if (Strings.isNullOrEmpty(message)) {
            logger.error("[还款优惠券收益回调MQ] ready to consume message: coupon repay message is null.");
            return;
        }
        logger.info("[还款优惠券收益回调MQ] ready to consume message: coupon repay callback.");
        BaseDto<PayDataDto> result = payWrapperClient.couponRepayCallbackAfterRepaySuccess(Long.parseLong(message));
        if (!result.isSuccess()) {
            logger.error("[还款优惠券收益回调MQ] coupon repay callback consume fail. notifyRequestId: " + message);
            throw new RuntimeException("coupon repay callback consume fail. notifyRequestId: " + message);
        }
        logger.info("[还款优惠券收益回调MQ] consume message success.");
    }
}
