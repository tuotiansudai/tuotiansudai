package com.tuotiansudai.mq.consumer.user;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.tuotiansudai.fudian.message.BankRechargeMessage;
import com.tuotiansudai.fudian.umpmessage.UmpRechargeMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.service.BankRechargeService;
import com.tuotiansudai.service.UmpRechargeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class UmpRechargeMessageConsumer implements MessageConsumer {
    private static Logger logger = LoggerFactory.getLogger(UmpRechargeMessageConsumer.class);

    private final UmpRechargeService umpRechargeService;

    @Autowired
    public UmpRechargeMessageConsumer(UmpRechargeService umpRechargeService) {
        this.umpRechargeService = umpRechargeService;
    }

    @Override
    public MessageQueue queue() {
        return MessageQueue.UmpRecharge_Success;
    }

    @Override
    public void consume(String message) {
        logger.info("[MQ] receive message: {}: {}.", this.queue(), message);

        if (Strings.isNullOrEmpty(message)) {
            logger.error("[MQ] Recharge_Success message is empty");
            return;
        }

        try {
            UmpRechargeMessage umpRechargeMessage = new Gson().fromJson(message, UmpRechargeMessage.class);
            umpRechargeService.processRecharge(umpRechargeMessage);
        } catch (JsonSyntaxException e) {
            logger.error(MessageFormat.format("[MQ] consume message error, message: {0}", message), e);
        }

    }

}