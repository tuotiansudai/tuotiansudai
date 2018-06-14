package com.tuotiansudai.mq.consumer.loan;

import com.google.common.base.Strings;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
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
import java.text.MessageFormat;

@Component
public class TransferReferrerRewardCallbackMessageConsumer implements MessageConsumer {
    private static Logger logger = LoggerFactory.getLogger(TransferReferrerRewardCallbackMessageConsumer.class);

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Override
    public MessageQueue queue() {
        return MessageQueue.TransferReferrerRewardCallback;
    }

    @Transactional
    @Override
    public void consume(String message) {
        logger.info("[推荐人奖励MQ] {} receive message: {}.", this.queue(), message);
        if (Strings.isNullOrEmpty(message)) {
            logger.error("[推荐人奖励MQ] message is empty");
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

                logger.error("[推荐人奖励MQ] message is invalid, message: {}", message);
                return;
            }
        } catch (IOException e) {
            logger.error("[推荐人奖励MQ] message can not convert to transferReferrerRewardCallbackMessage, message: {}", message);
            return;
        }

        try {
            BaseDto<PayDataDto> result = payWrapperClient.transferReferrerRewardCallBack(transferReferrerRewardCallbackMessage.getReferrerRewardId());

            if (!result.isSuccess()) {
                logger.error("[推荐人奖励MQ] callback consume is failed. message: {}", message);
                mqWrapperClient.sendMessage(MessageQueue.SmsFatalNotify, MessageFormat.format("推荐人奖励MQ错误: 消息处理失败, message: {0}", message));
            }
        } catch (Exception e) {
            logger.error(MessageFormat.format("[推荐人奖励MQ] callback consume is exception. message: {0}", message), e);
            mqWrapperClient.sendMessage(MessageQueue.SmsFatalNotify, MessageFormat.format("发放推荐人奖励回调错误, message: {0}", message));
            return;
        }

        logger.info("[推荐人奖励MQ] consume message success. message: {}", message);
    }
}
