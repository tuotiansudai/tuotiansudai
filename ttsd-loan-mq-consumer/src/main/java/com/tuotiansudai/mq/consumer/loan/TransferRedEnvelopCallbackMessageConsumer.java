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
import java.text.MessageFormat;

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
        logger.info("[投资红包MQ] {} receive message: {}.", this.queue(), message);
        if (Strings.isNullOrEmpty(message)) {
            logger.error("[投资红包MQ] receive message is empty");
            smsWrapperClient.sendFatalNotify(new SmsFatalNotifyDto("投资红包MQ错误: 消息为空"));
            return;
        }

        TransferRedEnvelopCallbackMessage transferRedEnvelopCallbackMessage;
        try {
            transferRedEnvelopCallbackMessage = JsonConverter.readValue(message, TransferRedEnvelopCallbackMessage.class);
            if (transferRedEnvelopCallbackMessage.getInvestId() == null
                    || transferRedEnvelopCallbackMessage.getLoanId() == null
                    || Strings.isNullOrEmpty(transferRedEnvelopCallbackMessage.getLoginName())
                    || transferRedEnvelopCallbackMessage.getUserCouponId() == null) {

                logger.error("[投资红包MQ] message {} is invalid ", message);
                smsWrapperClient.sendFatalNotify(new SmsFatalNotifyDto(MessageFormat.format("投资红包MQ错误: 无效消息, message: {0}", message)));
                return;
            }
        } catch (IOException e) {
            logger.error("[投资红包MQ] message can not convert to transferRedEnvelopCallbackMessage, message: {}", message);
            smsWrapperClient.sendFatalNotify(new SmsFatalNotifyDto(MessageFormat.format("投资红包MQ错误: 消息序反列化失败, message: {0}", message)));
            return;
        }

        try {
            BaseDto<PayDataDto> result = payWrapperClient.transferRedEnvelopForCallback(transferRedEnvelopCallbackMessage.getUserCouponId());

            if (!result.isSuccess()) {
                logger.error("[投资红包MQ] callback consume is failed. message: {}", message);
                smsWrapperClient.sendFatalNotify(new SmsFatalNotifyDto(MessageFormat.format("投资红包MQ错误: 消息处理失败, message: {0}", message)));
                return;
            }
        } catch (Exception e) {
            logger.error(MessageFormat.format("[投资红包MQ] message consume is exception. message: {0}", message), e);
            smsWrapperClient.sendFatalNotify(new SmsFatalNotifyDto(MessageFormat.format("投资红包MQ错误: 消息处理异常, message: {0}", message)));
            return;
        }

        logger.info("[投资红包MQ] consume message success, message: {}", message);
    }
}
