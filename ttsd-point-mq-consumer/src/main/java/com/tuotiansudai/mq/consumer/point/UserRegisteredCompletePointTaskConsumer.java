package com.tuotiansudai.mq.consumer.point;

import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.point.repository.mapper.UserPointMapper;
import com.tuotiansudai.point.repository.model.PointTask;
import com.tuotiansudai.point.repository.model.UserPointModel;
import com.tuotiansudai.point.service.PointTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Component
public class UserRegisteredCompletePointTaskConsumer implements MessageConsumer {
    private static Logger logger = LoggerFactory.getLogger(UserRegisteredCompletePointTaskConsumer.class);

    @Autowired
    private PointTaskService pointTaskService;

    @Autowired
    private UserPointMapper userPointMapper;

    @Override
    public MessageQueue queue() {
        return MessageQueue.UserRegistered_CompletePointTask;
    }

    @Transactional
    @Override
    public void consume(String message) {
        logger.info("[MQ] receive message: {}: '{}'.", this.queue(), message);
        if (!StringUtils.isEmpty(message)) {
            logger.info("[MQ] ready to consume message: complete user-registered task.");
            if (!userPointMapper.exists(message)) {
                userPointMapper.createIfNotExist(new UserPointModel(message, 0, 0, null));
            }
            pointTaskService.completeAdvancedTask(PointTask.EACH_RECOMMEND_REGISTER, message);
            logger.info("[MQ] consume message success.");
        }
    }
}
