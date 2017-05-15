package com.tuotiansudai.mq.consumer.activity;

import com.tuotiansudai.activity.service.DragonBoatFestivalService;
import com.tuotiansudai.client.MQWrapperClient;
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
    private MQWrapperClient mqWrapperClient;

    @Autowired
    private DragonBoatFestivalService dragonBoatFestivalService;

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
            String referrer = args[0];
            String loginName = args[1];

            // 给登录用户发送10元现金红包
            logger.info(MessageFormat.format("[MQ][Dragon boat: invite old user login] send ￥10 red enveloper to {}.", loginName));
            mqWrapperClient.sendMessage(MessageQueue.CouponAssigning, loginName + ":" + 421);

            // 给分享者增加邀请老用户数量
            logger.info(MessageFormat.format("[MQ][Dragon boat: invite old user login] add invite old user count for {}.", referrer));
            dragonBoatFestivalService.addInviteNewUserCount(referrer);
        }
    }
}
