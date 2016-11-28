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
public class RechargeSuccessCompletePointTaskConsumer implements MessageConsumer {
    private static Logger logger = LoggerFactory.getLogger(RechargeSuccessCompletePointTaskConsumer.class);

    @Autowired
    private PointTaskService pointTaskService;

    @Override
    public MessageQueue queue() {
        return MessageQueue.RechargeSuccess_CompletePointTask;
    }

    @Transactional
    @Override
    public void consume(String message) {
        logger.info("[MQ] receive message: {}: '{}'.", this.queue(), message);
        if (!StringUtils.isEmpty(message)) {
            logger.info("[MQ] ready to consume message: complete recharge task.");
            pointTaskService.completeNewbieTask(PointTask.FIRST_RECHARGE, message);
            logger.info("[MQ] consume message success.");
        }
    }
}
