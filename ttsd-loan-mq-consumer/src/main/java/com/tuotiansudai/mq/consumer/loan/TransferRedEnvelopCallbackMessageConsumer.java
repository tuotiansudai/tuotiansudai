package com.tuotiansudai.mq.consumer.loan;

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
import org.springframework.util.StringUtils;

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
        return MessageQueue.TransferReferrerRewardCallback;
    }

    @Transactional
    @Override
    public void consume(String message) {
        logger.info("[标的放款MQ] TransferReferrerRewardCallback receive message: {}: {}.", this.queue(), message);
        if (!StringUtils.isEmpty(message)) {
            TransferRedEnvelopCallbackMessage transferRedEnvelopCallbackMessage;
            try {
                transferRedEnvelopCallbackMessage = JsonConverter.readValue(message, TransferRedEnvelopCallbackMessage.class);
            } catch (IOException e) {
                logger.error("[标的放款MQ] TransferReferrerRewardCallback json convert transferRedEnvelopCallbackMessage is fail, message:{}", message);
                throw new RuntimeException(e);
            }

            logger.info("[标的放款MQ] TransferReferrerRewardCallback ready to consume message: loanId:{}, investId:{}, loginName:{}, userCouponId:{}",
                    transferRedEnvelopCallbackMessage.getLoanId(), transferRedEnvelopCallbackMessage.getInvestId(),
                    transferRedEnvelopCallbackMessage.getLoginName(), transferRedEnvelopCallbackMessage.getUserCouponId());
            BaseDto<PayDataDto> result;
            try {
                result = payWrapperClient.transferRedEnvelopForCallBack(transferRedEnvelopCallbackMessage.getUserCouponId());
            }catch (Exception e){
                result = new BaseDto<>(false);
                logger.error("[标的放款MQ] TransferReferrerRewardCallback callback consume fail. message: " + e);
            }
            if (!result.isSuccess()) {
                logger.error("[标的放款MQ] TransferReferrerRewardCallback callback consume fail. notifyRequestId: " + message);
                smsWrapperClient.sendFatalNotify(new SmsFatalNotifyDto("发放推荐人奖励回调错误"));
                throw new RuntimeException("[标的放款MQ] TransferReferrerRewardCallback callback consume fail. notifyRequestId: " + message);
            }
            logger.info("[标的放款MQ] TransferReferrerRewardCallback consume message success.");
        }
    }
}
