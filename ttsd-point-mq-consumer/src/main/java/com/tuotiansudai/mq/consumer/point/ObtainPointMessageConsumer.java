package com.tuotiansudai.mq.consumer.point;

import com.tuotiansudai.message.ObtainPointMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.point.repository.mapper.UserPointMapper;
import com.tuotiansudai.util.JsonConverter;
import com.tuotiansudai.util.RedisWrapperClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Date;

@Component
public class ObtainPointMessageConsumer implements MessageConsumer {
    private static Logger logger = LoggerFactory.getLogger(ObtainPointMessageConsumer.class);

    private static final String FROZEN_POINT_KEY = "FROZEN:POINT:%s";

    @Autowired
    private UserPointMapper userPointMapper;

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    @Override
    public MessageQueue queue() {
        return MessageQueue.ObtainPoint;
    }

    @Transactional
    @Override
    public void consume(String message) {

        logger.info("[MQ] receive message: {}: {}.", this.queue(), message);
        if (!StringUtils.isEmpty(message)) {
            String loginName;
            long point;
            ObtainPointMessage obtainPointMessage;
            try {
                obtainPointMessage = JsonConverter.readValue(message, ObtainPointMessage.class);
                loginName = obtainPointMessage.getLoginName();
                point = obtainPointMessage.getPoint();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            try {
                logger.info("[MQ] ready to consume message: . loginName:{}, point:{}", loginName, point);
                userPointMapper.increaseOrCreate(loginName, point);
                if (point < 0) {
                    logger.info(String.format("[MQ] loginName:%s unfreeze point:%s", loginName, point));
                    redisWrapperClient.incr(String.format(FROZEN_POINT_KEY, loginName), point);
                }
            } catch (Exception e) {
                logger.error(String.format("consume ObtainPoint message fail loginName:%s,point:%s", loginName, String.valueOf(point)), e);
            }
        }
        logger.info("[MQ] consume message success.");
    }
}