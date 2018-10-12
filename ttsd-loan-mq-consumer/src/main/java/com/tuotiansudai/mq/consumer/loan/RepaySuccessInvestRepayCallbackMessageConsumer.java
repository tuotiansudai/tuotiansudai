package com.tuotiansudai.mq.consumer.loan;

import com.google.common.base.Strings;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.message.RepaySuccessAsyncCallBackMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.util.JsonConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Component
public class RepaySuccessInvestRepayCallbackMessageConsumer implements MessageConsumer {
    private static Logger logger = LoggerFactory.getLogger(RepaySuccessInvestRepayCallbackMessageConsumer.class);

    @Autowired
    private PayWrapperClient payWrapperClient;
    private MQWrapperClient mqWrapperClient;

    @Override
    public MessageQueue queue() {
        return MessageQueue.RepaySuccessInvestRepayCallback;
    }

    @Transactional
    @Override
    public void consume(String message) {
        logger.info("[还款发放出借人收益回调MQ] receive message: {}: {}.", this.queue(), message);
        if (Strings.isNullOrEmpty(message)) {
            logger.error("[还款发放出借人收益回调MQ] RepaySuccessInvestRepayCallback receive message is empty");
            return;
        }
        RepaySuccessAsyncCallBackMessage repaySuccessAsyncCallBackMessage;
        try {
            repaySuccessAsyncCallBackMessage = JsonConverter.readValue(message, RepaySuccessAsyncCallBackMessage.class);
        } catch (IOException e) {
            logger.error("[还款发放出借人收益回调MQ] RepaySuccessInvestRepayCallback json convert RepaySuccessMessage is fail, message:{}", message);
            return;
        }
        try {
            logger.info("[还款发放出借人收益回调MQ] RepaySuccessInvestRepayCallback ready to consume message: repay callback.");
            BaseDto<PayDataDto> result = payWrapperClient.repayInvestPayback(repaySuccessAsyncCallBackMessage);
            if (!result.isSuccess()) {
                logger.error("RepaySuccessInvestRepayCallback invest repay callback consume fail. notifyRequestId: " + message);
                throw new RuntimeException("invest repay callback consume fail. notifyRequestId: " + message);
            }

        } catch (Exception e) {
            logger.error("[还款发放出借人收益回调MQ] RepaySuccessInvestRepayCallback  is fail, message:{}", message);
            mqWrapperClient.sendMessage(MessageQueue.SmsFatalNotify, "还款发放出借人收益回调失败, 业务处理异常");
            return;
        }
        logger.info("[还款发放出借人收益回调MQ] RepaySuccessInvestRepayCallback consume message success.");
    }
}
