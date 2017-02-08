package com.tuotiansudai.mq.consumer.loan;

import com.google.common.base.Strings;
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
        return MessageQueue.TransferReferrerRewardCallback;
    }

    @Transactional
    @Override
    public void consume(String message) {
        logger.info("[标的放款MQ] TransferReferrerRewardCallback receive message: {}: {}.", this.queue(), message);
        if (Strings.isNullOrEmpty(message)) {
            logger.error("[标的放款MQ] TransferReferrerRewardCallback receive message is empty");
            smsWrapperClient.sendFatalNotify(new SmsFatalNotifyDto("发放现金红包回调错误, MQ消息为空"));
            return;
        }

        TransferReferrerRewardCallbackMessage transferReferrerRewardCallbackMessage;
        try {
            transferReferrerRewardCallbackMessage = JsonConverter.readValue(message, TransferReferrerRewardCallbackMessage.class);
            if (transferReferrerRewardCallbackMessage.getInvestId() == null
                    || transferReferrerRewardCallbackMessage.getLoanId() == null
                    || transferReferrerRewardCallbackMessage.getReferrerRewardId() == null
                    || Strings.isNullOrEmpty(transferReferrerRewardCallbackMessage.getLoginName())
                    || Strings.isNullOrEmpty(transferReferrerRewardCallbackMessage.getReferrer())) {

                logger.error("[标的放款MQ] TransferReferrerRewardCallback data is empty");
                smsWrapperClient.sendFatalNotify(new SmsFatalNotifyDto("发放现金红包回调错误, 消息中推荐人相关信息为空"));
                return;
            }
        } catch (IOException e) {
            logger.error("[标的放款MQ] TransferReferrerRewardCallback json convert transferReferrerRewardCallbackMessage is fail, message:{}", message);
            smsWrapperClient.sendFatalNotify(new SmsFatalNotifyDto("发放现金红包回调错误, 解析消息失败"));
            return;
        }

        logger.info("[标的放款MQ] TransferReferrerRewardCallback ready to consume message: loanId:{}, investId:{}, referrer:{}, loginName:{}, referrerRewardId:{}",
                transferReferrerRewardCallbackMessage.getLoanId(), transferReferrerRewardCallbackMessage.getInvestId(),
                transferReferrerRewardCallbackMessage.getReferrer(), transferReferrerRewardCallbackMessage.getLoginName(),
                transferReferrerRewardCallbackMessage.getReferrerRewardId());

        BaseDto<PayDataDto> result;
        try {
            result = payWrapperClient.transferReferrerRewardCallBack(transferReferrerRewardCallbackMessage.getReferrerRewardId());
        } catch (Exception e) {
            logger.error("[标的放款MQ] TransferReferrerRewardCallback callback consume fail. message: " + e);
            smsWrapperClient.sendFatalNotify(new SmsFatalNotifyDto("发放现金红包回调错误, 业务处理异常"));
            return;
        }

        if (!result.isSuccess()) {
            logger.error("[标的放款MQ] TransferReferrerRewardCallback callback consume fail. notifyRequestId: " + message);
            smsWrapperClient.sendFatalNotify(new SmsFatalNotifyDto("发放现金红包回调错误"));
            throw new RuntimeException("[标的放款MQ] TransferReferrerRewardCallback callback consume fail. notifyRequestId: " + message);
        }

        logger.info("[标的放款MQ] TransferReferrerRewardCallback consume message success.");
    }
}
