package com.tuotiansudai.mq.consumer.point;

import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.point.repository.model.PointTask;
import com.tuotiansudai.point.service.PointTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Component
public class BindBankCardCompletePointTaskConsumer implements MessageConsumer {
    private static Logger logger = LoggerFactory.getLogger(BindBankCardCompletePointTaskConsumer.class);

    @Autowired
    private PointTaskService pointTaskService;

    @Override
    public MessageQueue queue() {
        return MessageQueue.BindBankCard_CompletePointTask;
    }

    @Transactional
    @Override
    public void consume(String message) {
        logger.info("[MQ] receive message: {}: '{}'.", this.queue(), message);
        if (!StringUtils.isEmpty(message)) {
            logger.info("[MQ] ready to consume message: complete bind-bank-card task.");
            pointTaskService.completeNewbieTask(PointTask.BIND_BANK_CARD, message);
            logger.info("[MQ] consume message success.");
        }
    }
}
