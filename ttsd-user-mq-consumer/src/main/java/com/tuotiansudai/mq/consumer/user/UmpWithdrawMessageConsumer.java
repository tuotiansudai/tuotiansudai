package com.tuotiansudai.mq.consumer.user;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.tuotiansudai.fudian.umpmessage.UmpRechargeMessage;
import com.tuotiansudai.fudian.umpmessage.UmpWithdrawMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.service.UmpRechargeService;
import com.tuotiansudai.service.UmpWithdrawService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class UmpWithdrawMessageConsumer implements MessageConsumer {
    private static Logger logger = LoggerFactory.getLogger(UmpWithdrawMessageConsumer.class);

    private final UmpWithdrawService umpWithdrawService;

    @Autowired
    public UmpWithdrawMessageConsumer(UmpWithdrawService umpWithdrawService) {
        this.umpWithdrawService = umpWithdrawService;
    }

    @Override
    public MessageQueue queue() {
        return MessageQueue.UmpWithdraw_Success;
    }

    @Override
    public void consume(String message) {
        logger.info("[MQ] receive message: {}: {}.", this.queue(), message);

        if (Strings.isNullOrEmpty(message)) {
            logger.error("[MQ] UmpWithdraw_Success message is empty");
            return;
        }

        try {
            UmpWithdrawMessage umpWithdrawMessage = new Gson().fromJson(message, UmpWithdrawMessage.class);
            umpWithdrawService.processWithdraw(umpWithdrawMessage);
        } catch (JsonSyntaxException e) {
            logger.error(MessageFormat.format("[MQ] consume message error, message: {0}", message), e);
        }

    }

}