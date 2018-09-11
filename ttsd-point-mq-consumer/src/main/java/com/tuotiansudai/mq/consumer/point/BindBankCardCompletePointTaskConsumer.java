package com.tuotiansudai.mq.consumer.point;

import com.google.gson.Gson;
import com.tuotiansudai.fudian.message.BankBindCardMessage;
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
public class BindBankCardCompletePointTaskConsumer implements MessageConsumer {

    private static Logger logger = LoggerFactory.getLogger(BindBankCardCompletePointTaskConsumer.class);

    private final PointTaskService pointTaskService;

    @Autowired
    public BindBankCardCompletePointTaskConsumer(PointTaskService pointTaskService) {
        this.pointTaskService = pointTaskService;
    }

    @Override
    public MessageQueue queue() {
        return MessageQueue.BindBankCard_CompletePointTask;
    }

    @Override
    public void consume(String message) {
        logger.info("[MQ] receive message: {}: '{}'.", this.queue(), message);

        try {
            BankBindCardMessage bankBindCardMessage = new Gson().fromJson(message, BankBindCardMessage.class);
            this.pointTaskService.completeNewbieTask(PointTask.BIND_BANK_CARD, bankBindCardMessage.getLoginName());
        } catch (Exception e) {
            logger.error(MessageFormat.format("[MQ] consume message error, message: {0}", message), e);
        }
    }
}
