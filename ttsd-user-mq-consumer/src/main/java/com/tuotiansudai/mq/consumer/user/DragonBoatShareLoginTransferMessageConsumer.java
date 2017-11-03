package com.tuotiansudai.mq.consumer.user;

import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;

@Component
public class DragonBoatShareLoginTransferMessageConsumer implements MessageConsumer {
    private static Logger logger = LoggerFactory.getLogger(DragonBoatShareLoginTransferMessageConsumer.class);

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Autowired
    private UserMapper userMapper;

    @Override
    public MessageQueue queue() {
        return MessageQueue.DragonBoatShareLoginTransfer;
    }

    @Transactional
    @Override
    public void consume(String message) {
        logger.info("[MQ] receive message: {}: {}.", this.queue(), message);
        if (!StringUtils.isEmpty(message)) {

            String referrer = message;

            UserModel userModel = userMapper.findByLoginName(referrer);
            mqWrapperClient.sendMessage(MessageQueue.DragonBoatShareLogin,
                    MessageFormat.format("{0}:{1}:{2}", userModel.getLoginName(), userModel.getUserName(), userModel.getMobile()));
        }
        logger.info("[MQ] receive message: {}: {}. done.", this.queue(), message);
    }
}
