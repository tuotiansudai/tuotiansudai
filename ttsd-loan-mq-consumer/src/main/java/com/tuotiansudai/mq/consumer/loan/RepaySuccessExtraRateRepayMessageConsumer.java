package com.tuotiansudai.mq.consumer.loan;

import com.google.common.base.Strings;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.sms.SmsFatalNotifyDto;
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
    private SmsWrapperClient smsWrapperClient;

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
            smsWrapperClient.sendFatalNotify(new SmsFatalNotifyDto("还款发放阶梯加息收益失败, MQ消息为空"));
            return;
        }

        RepaySuccessMessage repaySuccessMessage;

        try {
            repaySuccessMessage = JsonConverter.readValue(message, RepaySuccessMessage.class);
        } catch (IOException e) {
            logger.error("[还款发放阶梯加息收益MQ] RepaySuccess_ExtraRateRepay json convert RepaySuccessMessage is fail, message:{}", message);
            smsWrapperClient.sendFatalNotify(new SmsFatalNotifyDto("还款发放阶梯加息收益失败, MQ消息解析失败!"));
            return;
        }

        Long loanRepayId = repaySuccessMessage.getLoanRepayId();
        if (loanRepayId == null) {
            logger.error("[还款发放阶梯加息收益MQ] RepaySuccess_ExtraRateRepay loanRepayId is null, message:{}", message);
            smsWrapperClient.sendFatalNotify(new SmsFatalNotifyDto("还款发放阶梯加息收益失败, 还款ID为空!"));
            return;
        }

        try {
            BaseDto<PayDataDto> baseDto = payWrapperClient.extraRateNormalRepayAfterRepaySuccess(repaySuccessMessage);

            if (!baseDto.isSuccess()) {
                smsWrapperClient.sendFatalNotify(new SmsFatalNotifyDto(MessageFormat.format("还款发放优惠券收益失败,还款ID:{0}", String.valueOf(loanRepayId))));
                logger.error("[还款发放阶梯加息收益MQ] RepaySuccess_ExtraRateRepay is fail. loanId:{}", String.valueOf(loanRepayId));
                throw new RuntimeException("[还款发放阶梯加息收益MQ] RepaySuccess_ExtraRateRepay is fail. loanOutInfo: " + message);
            }
        } catch (Exception e) {
            logger.error("[还款发放阶梯加息收益MQ] RepaySuccess_ExtraRateRepay  is fail, message:{}", message);
            smsWrapperClient.sendFatalNotify(new SmsFatalNotifyDto("还款发放阶梯加息收益失败, 业务处理异常"));
            return;
        }

        logger.info("[还款发放阶梯加息收益MQ] RepaySuccess_ExtraRateRepay consume success.");
    }
}
