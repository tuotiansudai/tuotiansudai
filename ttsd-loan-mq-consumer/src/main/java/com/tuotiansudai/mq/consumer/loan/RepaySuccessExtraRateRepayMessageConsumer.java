package com.tuotiansudai.mq.consumer.loan;

import com.google.common.base.Strings;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.message.RepaySuccessMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.util.JsonConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.MessageFormat;

@Component
public class RepaySuccessExtraRateRepayMessageConsumer implements MessageConsumer {

    private static Logger logger = LoggerFactory.getLogger(RepaySuccessExtraRateRepayMessageConsumer.class);

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Override
    public MessageQueue queue() {
        return MessageQueue.RepaySuccess_ExtraRateRepay;
    }

    @Override
    public void consume(String message) {

        logger.info("[还款发放阶梯加息收益MQ] RepaySuccess_ExtraRateRepay receive message: {}: {}.", this.queue(), message);

        if (Strings.isNullOrEmpty(message)) {
            logger.error("[还款发放阶梯加息收益MQ] RepaySuccess_ExtraRateRepay receive message is empty");
            return;
        }

        RepaySuccessMessage repaySuccessMessage;

        try {
            repaySuccessMessage = JsonConverter.readValue(message, RepaySuccessMessage.class);
        } catch (IOException e) {
            logger.error("[还款发放阶梯加息收益MQ] RepaySuccess_ExtraRateRepay json convert RepaySuccessMessage is fail, message:{}", message);
            return;
        }

        Long loanRepayId = repaySuccessMessage.getLoanRepayId();
        if (loanRepayId == null) {
            logger.error("[还款发放阶梯加息收益MQ] RepaySuccess_ExtraRateRepay loanRepayId is null, message:{}", message);
            return;
        }

        try {
            BaseDto<PayDataDto> baseDto = payWrapperClient.extraRateRepayAfterRepaySuccess(repaySuccessMessage);

            if (!baseDto.isSuccess()) {
                logger.error("RepaySuccess_ExtraRateRepay consume fail. message: " + message);
                throw new RuntimeException("RepaySuccess_ExtraRateRepay consume fail. message: " + message);
            }
            if (!baseDto.getData().getStatus()) {
                logger.error("[还款发放阶梯加息收益MQ] RepaySuccess_ExtraRateRepay is fail. loanId:{}", String.valueOf(loanRepayId));
                mqWrapperClient.sendMessage(MessageQueue.SmsFatalNotify, MessageFormat.format("还款发放优惠券收益失败,还款ID:{0}", String.valueOf(loanRepayId)));
            }
        } catch (Exception e) {
            logger.error("[还款发放阶梯加息收益MQ] RepaySuccess_ExtraRateRepay  is fail, message:{}", message);
            mqWrapperClient.sendMessage(MessageQueue.SmsFatalNotify, "还款发放阶梯加息收益失败, 业务处理异常");
            return;
        }

        logger.info("[还款发放阶梯加息收益MQ] RepaySuccess_ExtraRateRepay consume success.");
    }
}
