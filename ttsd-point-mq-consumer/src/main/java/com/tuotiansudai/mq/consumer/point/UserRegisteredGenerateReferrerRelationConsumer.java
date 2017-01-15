package com.tuotiansudai.mq.consumer.point;

import com.google.common.base.Strings;
import com.tuotiansudai.exception.ReferrerRelationException;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.service.ReferrerRelationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class UserRegisteredGenerateReferrerRelationConsumer implements MessageConsumer {
    private static Logger logger = LoggerFactory.getLogger(UserRegisteredGenerateReferrerRelationConsumer.class);

    private final UserMapper userMapper;

    private final ReferrerRelationService referrerRelationService;

    @Autowired
    public UserRegisteredGenerateReferrerRelationConsumer(UserMapper userMapper, ReferrerRelationService referrerRelationService) {
        this.userMapper = userMapper;
        this.referrerRelationService = referrerRelationService;
    }

    @Override
    public MessageQueue queue() {
        return MessageQueue.UserRegistered_GenerateReferrerRelation;
    }

    @Override
    public void consume(String message) {
        logger.info("[UserRegistered_GenerateReferrerRelation MQ] receive message: '{}'", message);
        if (Strings.isNullOrEmpty(message)) {
            logger.error("[UserRegistered_GenerateReferrerRelation MQ] message is empty");
            return;
        }

        UserModel newRegisteredUser = userMapper.findByLoginNameOrMobile(message);
        if (newRegisteredUser == null || Strings.isNullOrEmpty(newRegisteredUser.getReferrer())) {
            logger.error("[UserRegistered_GenerateReferrerRelation MQ] login name({}) not found or referrer is empty, ", message);
            return;
        }

        try {
            this.referrerRelationService.generateRelation(newRegisteredUser.getReferrer(), newRegisteredUser.getLoginName());
        } catch (ReferrerRelationException e) {
            logger.error(MessageFormat.format("[UserRegistered_GenerateReferrerRelation MQ] receive message: '{0}'", message), e);
            throw new RuntimeException(MessageFormat.format("[UserRegistered_GenerateReferrerRelation MQ] receive message: '{0}'", message), e);

        }
        logger.info("[UserRegistered_GenerateReferrerRelation MQ] receive message: '{0}'. finished", message);
    }
}
