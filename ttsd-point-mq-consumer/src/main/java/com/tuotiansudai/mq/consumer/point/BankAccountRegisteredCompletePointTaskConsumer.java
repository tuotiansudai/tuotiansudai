package com.tuotiansudai.mq.consumer.point;


import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.tuotiansudai.fudian.message.BankRegisterMessage;
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
public class BankAccountRegisteredCompletePointTaskConsumer implements MessageConsumer {

    private static Logger logger = LoggerFactory.getLogger(BankAccountRegisteredCompletePointTaskConsumer.class);

    @Autowired
    public PointTaskService pointTaskService;

    @Override
    public MessageQueue queue() {
        return MessageQueue.RegisterBankAccount_CompletePointTask;
    }

    @Override
    public void consume(String message) {
        logger.info("[MQ] receive message: {}: {}.", this.queue(), message);

        if (Strings.isNullOrEmpty(message)) {
            logger.error("[MQ] RechargeSuccess_CompletePointTask message is empty");
            return;
        }

        try {
            BankRegisterMessage bankRegisterMessage = new Gson().fromJson(message, BankRegisterMessage.class);
            pointTaskService.completeNewbieTask(PointTask.REGISTER, bankRegisterMessage.getLoginName());

        } catch (Exception e) {
            logger.error(MessageFormat.format("[MQ] consume message error, message: {0}", message), e);
        }
    }
}
