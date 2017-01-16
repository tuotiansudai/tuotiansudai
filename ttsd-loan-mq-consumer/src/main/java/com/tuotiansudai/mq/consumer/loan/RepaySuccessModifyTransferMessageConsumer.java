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
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.MessageFormat;

@Component
public class RepaySuccessModifyTransferMessageConsumer implements MessageConsumer {
    private static Logger logger = LoggerFactory.getLogger(RepaySuccessModifyTransferMessageConsumer.class);

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Autowired
    private SmsWrapperClient smsWrapperClient;

    @Override
    public MessageQueue queue() {
        return MessageQueue.RepaySuccess_ModifyTransfer;
    }

    @Transactional
    @Override
    public void consume(String message) {
        logger.info("[还款MQ] receive message: {}: {}.", this.queue(), message);

        if (Strings.isNullOrEmpty(message)) {
            logger.error("[还款MQ] RepaySuccess_ModifyTransfer receive message is empty");
            return;
        }

        RepaySuccessMessage repaySuccessMessage;
        try {
            repaySuccessMessage = JsonConverter.readValue(message, RepaySuccessMessage.class);
            if (repaySuccessMessage.getLoanRepayId() == null) {
                logger.error("[还款MQ] RepaySuccess_ModifyTransfer loanRepayId is empty, message:{}", message);
                smsWrapperClient.sendFatalNotify(new SmsFatalNotifyDto("放款修改债权转让状态失败,loanRepayId为空"));
                return;
            }
        } catch (IOException e) {
            logger.error("[还款MQ] RepaySuccess_ModifyTransfer json convert RepaySuccessMessage fail, message:{}", message);
            smsWrapperClient.sendFatalNotify(new SmsFatalNotifyDto("放款修改债权转让状态失败,解析消息失败"));
            return;
        }

        logger.info("[还款MQ] ready to consume message: .");
        BaseDto<PayDataDto> result = payWrapperClient.advanceTransfer(repaySuccessMessage.getLoanRepayId());
        if (!result.isSuccess()) {
            logger.error("[还款MQ] RepaySuccess_ModifyTransfer consume fail. loanRepayId: " + message);
            smsWrapperClient.sendFatalNotify(new SmsFatalNotifyDto(MessageFormat.format("放款修改债权转让状态失败, loanRepayId:{0}", String.valueOf(repaySuccessMessage.getLoanRepayId()))));
            throw new RuntimeException("invest callback consume fail. loanRepayId: " + message);
        }

        logger.info("[还款MQ] consume message success.");
    }
}
