package com.tuotiansudai.mq.consumer.loan;

import com.google.common.base.Strings;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.sms.SmsFatalNotifyDto;
import com.tuotiansudai.message.TransferRedEnvelopCallbackMessage;
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
public class TransferRedEnvelopCallbackMessageConsumer implements MessageConsumer {
    private static Logger logger = LoggerFactory.getLogger(TransferRedEnvelopCallbackMessageConsumer.class);

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
        if (Strings.isNullOrEmpty(message)) {
            logger.error("[标的放款MQ] TransferRedEnvelopCallback receive message is empty");
            smsWrapperClient.sendFatalNotify(new SmsFatalNotifyDto("发放推荐人奖励回调错误, MQ消息为空"));
            return;
        }

        TransferRedEnvelopCallbackMessage transferRedEnvelopCallbackMessage;
        try {
            transferRedEnvelopCallbackMessage = JsonConverter.readValue(message, TransferRedEnvelopCallbackMessage.class);
            if (transferRedEnvelopCallbackMessage.getInvestId() == null
                    || transferRedEnvelopCallbackMessage.getLoanId() == null
                    || Strings.isNullOrEmpty(transferRedEnvelopCallbackMessage.getLoginName())
                    || transferRedEnvelopCallbackMessage.getUserCouponId() == null) {

                logger.error("[标的放款MQ] TransferRedEnvelopCallback data is empty");
                smsWrapperClient.sendFatalNotify(new SmsFatalNotifyDto("发放推荐人奖励回调错误, 消息中推荐人相关信息为空"));
                return;
            }
        } catch (IOException e) {
            logger.error("[标的放款MQ] TransferRedEnvelopCallback json convert transferRedEnvelopCallbackMessage is fail, message:{}", message);
            smsWrapperClient.sendFatalNotify(new SmsFatalNotifyDto("发放推荐人奖励回调错误, 解析消息失败"));
            return;
        }

        logger.info("[标的放款MQ] TransferRedEnvelopCallback ready to consume message: loanId:{}, investId:{}, loginName:{}, userCouponId:{}",
                transferRedEnvelopCallbackMessage.getLoanId(), transferRedEnvelopCallbackMessage.getInvestId(),
                transferRedEnvelopCallbackMessage.getLoginName(), transferRedEnvelopCallbackMessage.getUserCouponId());

        BaseDto<PayDataDto> result;
        try {
            result = payWrapperClient.transferRedEnvelopForCallBack(transferRedEnvelopCallbackMessage.getUserCouponId());
        } catch (Exception e) {
            logger.error("[标的放款MQ] TransferRedEnvelopCallback callback consume fail. message: " + e);
            smsWrapperClient.sendFatalNotify(new SmsFatalNotifyDto("发放推荐人奖励回调错误, 业务处理异常"));
            return;
        }

        if (!result.isSuccess()) {
            logger.error("[标的放款MQ] TransferRedEnvelopCallback callback consume fail. notifyRequestId: " + message);
            smsWrapperClient.sendFatalNotify(new SmsFatalNotifyDto("发放推荐人奖励回调错误"));
            throw new RuntimeException("[标的放款MQ] TransferRedEnvelopCallback callback consume fail. notifyRequestId: " + message);
        }

        logger.info("[标的放款MQ] TransferRedEnvelopCallback consume message success.");
    }
}
