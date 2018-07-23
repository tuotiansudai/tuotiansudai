package com.tuotiansudai.mq.consumer.user;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.tuotiansudai.fudian.umpmessage.UmpBindCardMessage;
import com.tuotiansudai.fudian.umpmessage.UmpRechargeMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.service.UmpBindCardService;
import com.tuotiansudai.service.UmpRechargeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class UmpBindCardMessageConsumer implements MessageConsumer {
    private static Logger logger = LoggerFactory.getLogger(UmpBindCardMessageConsumer.class);

    private final UmpBindCardService umpBindCardService;

    @Autowired
    public UmpBindCardMessageConsumer(UmpBindCardService umpBindCardService) {
        this.umpBindCardService = umpBindCardService;
    }

    @Override
    public MessageQueue queue() {
        return MessageQueue.UmpBindCard_Success;
    }

    @Override
    public void consume(String message) {
        logger.info("[MQ] receive message: {}: {}.", this.queue(), message);

        if (Strings.isNullOrEmpty(message)) {
            logger.error("[MQ] Recharge_Success message is empty");
            return;
        }

        try {
            UmpBindCardMessage umpBindCardMessage = new Gson().fromJson(message, UmpBindCardMessage.class);
            umpBindCardService.processBindCard(umpBindCardMessage);
        } catch (JsonSyntaxException e) {
            logger.error(MessageFormat.format("[MQ] consume message error, message: {0}", message), e);
        }
    }
}