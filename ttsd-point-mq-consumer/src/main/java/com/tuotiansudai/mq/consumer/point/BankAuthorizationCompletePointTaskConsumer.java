package com.tuotiansudai.mq.consumer.point;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.tuotiansudai.fudian.message.BankAuthorizationMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.point.repository.model.PointTask;
import com.tuotiansudai.point.service.PointTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class BankAuthorizationCompletePointTaskConsumer implements MessageConsumer{

    private static Logger logger = LoggerFactory.getLogger(BankAuthorizationCompletePointTaskConsumer.class);

    private final PointTaskService pointTaskService;

    @Autowired
    public BankAuthorizationCompletePointTaskConsumer(PointTaskService pointTaskService){
        this.pointTaskService = pointTaskService;
    }

    @Override
    public MessageQueue queue() {
        return MessageQueue.Authorization_CompletePointTask;
    }

    @Override
    public void consume(String message) {
        logger.info("[MQ] receive message: {}: {}.", this.queue(), message);

        if (Strings.isNullOrEmpty(message)) {
            logger.error("[MQ] Authorization_Success message is empty");
            return;
        }

        try {
            BankAuthorizationMessage bankAuthorizationMessage = new Gson().fromJson(message, BankAuthorizationMessage.class);
            pointTaskService.completeAdvancedTask(PointTask.FIRST_TURN_ON_NO_PASSWORD_INVEST, bankAuthorizationMessage.getLoginName());

        } catch (Exception e) {
            logger.error(MessageFormat.format("[MQ] consume message error, message: {0}", message), e);
        }
    }
}
