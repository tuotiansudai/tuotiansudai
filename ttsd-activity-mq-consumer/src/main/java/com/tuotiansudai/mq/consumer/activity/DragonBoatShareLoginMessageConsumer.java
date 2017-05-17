package com.tuotiansudai.mq.consumer.activity;

import com.tuotiansudai.activity.repository.mapper.DragonBoatFestivalMapper;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;


@Component
public class DragonBoatShareLoginMessageConsumer implements MessageConsumer {

    private static Logger logger = LoggerFactory.getLogger(DragonBoatShareLoginMessageConsumer.class);

    @Autowired
    private DragonBoatFestivalMapper dragonBoatFestivalMapper;

    @Override
    public MessageQueue queue() {
        return MessageQueue.DragonBoatShareLogin;
    }

    @Transactional
    @Override
    public void consume(String message) {
        logger.info("[MQ] receive message: {}: {}.", this.queue(), message);
        if (!StringUtils.isEmpty(message)) {

            String[] args = message.split(":");
            String loginName = args[0];
            String userName = args[1];
            String mobile = args[2];

            // 给分享者增加邀请老用户数量
            logger.info(MessageFormat.format("[MQ][Dragon boat: invite old user login] add invite old user count for {}.", loginName));
            dragonBoatFestivalMapper.addInviteOldUserCount(loginName, userName, mobile);
        }
        logger.info("[MQ] receive message: {}: {}. done.", this.queue(), message);
    }
}
