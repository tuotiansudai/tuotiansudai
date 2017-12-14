package com.tuotiansudai.mq.consumer.user;

import com.tuotiansudai.message.ObtainPointMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.util.JsonConverter;
import com.tuotiansudai.util.RedisWrapperClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Component
public class ObtainPointMessageConsumer implements MessageConsumer {
    private static Logger logger = LoggerFactory.getLogger(ObtainPointMessageConsumer.class);

    private static final String POINT_TRANSACTION_KEY = "POINT:HEALTH:REPORT:%s";

    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private RedisWrapperClient redisWrapperClient;

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
            ObtainPointMessage obtainPointMessage;
            try {
                obtainPointMessage = JsonConverter.readValue(message, ObtainPointMessage.class);
                loginName = obtainPointMessage.getLoginName();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            try {
                long point = obtainPointMessage.getPoint();
                AccountModel accountModel = accountMapper.lockByLoginName(loginName);
                accountModel.setPoint(accountModel.getPoint() + point);
                logger.info("[MQ] ready to consume message: . loginName:{}, point:{}", loginName, point);
                accountMapper.update(accountModel);
            } catch (Exception e) {
                logger.error("consume ObtainPoint message fail", e);
            }
            redisWrapperClient.hdel(POINT_TRANSACTION_KEY, loginName);
        }
        logger.info("[MQ] consume message success.");
    }
}