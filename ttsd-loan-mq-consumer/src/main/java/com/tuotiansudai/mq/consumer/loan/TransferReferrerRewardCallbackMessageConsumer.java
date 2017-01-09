package com.tuotiansudai.mq.consumer.loan;

import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.sms.SmsFatalNotifyDto;
import com.tuotiansudai.message.TransferReferrerRewardCallbackMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.util.JsonConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Component
public class TransferReferrerRewardCallbackMessageConsumer implements MessageConsumer {
    private static Logger logger = LoggerFactory.getLogger(TransferReferrerRewardCallbackMessageConsumer.class);

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Autowired
    private SmsWrapperClient smsWrapperClient;

    @Override
    public MessageQueue queue() {
        return MessageQueue.TransferRedEnvelopCallback;
    }

    @Transactional
    @Override
    public void consume(String message) {
        logger.info("[标的放款MQ] TransferRedEnvelopCallback receive message: {}: {}.", this.queue(), message);
        if (!StringUtils.isEmpty(message)) {

            TransferReferrerRewardCallbackMessage transferReferrerRewardCallbackMessage;
            try {
                transferReferrerRewardCallbackMessage = JsonConverter.readValue(message, TransferReferrerRewardCallbackMessage.class);
            } catch (IOException e) {
                logger.error("[标的放款MQ] TransferRedEnvelopCallback json convert transferReferrerRewardCallbackMessage is fail, message:{}", message);
                throw new RuntimeException(e);
            }

            logger.info("[标的放款MQ] TransferRedEnvelopCallback ready to consume message: loanId:{}, investId:{}, referrer:{}, loginName:{}, referrerRewardId:{}",
                    transferReferrerRewardCallbackMessage.getLoanId(), transferReferrerRewardCallbackMessage.getInvestId(),
                    transferReferrerRewardCallbackMessage.getReferrer(), transferReferrerRewardCallbackMessage.getLoginName(),
                    transferReferrerRewardCallbackMessage.getReferrerRewardId());
            BaseDto<PayDataDto> result = payWrapperClient.transferReferrerRewardCallBack(transferReferrerRewardCallbackMessage.getReferrerRewardId());
            if (!result.isSuccess()) {
                logger.error("[标的放款MQ] TransferRedEnvelopCallback callback consume fail. notifyRequestId: " + message);
                smsWrapperClient.sendFatalNotify(new SmsFatalNotifyDto("发放现金红包回调错误"));
                throw new RuntimeException("[标的放款MQ] TransferRedEnvelopCallback callback consume fail. notifyRequestId: " + message);
            }
            logger.info("[标的放款MQ] TransferRedEnvelopCallback consume message success.");
        }
    }
}
