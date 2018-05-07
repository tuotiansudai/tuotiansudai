package com.tuotiansudai.mq.consumer.message;


import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tuotiansudai.enums.MessageEventType;
import com.tuotiansudai.message.repository.mapper.MessageMapper;
import com.tuotiansudai.message.repository.model.MessageModel;
import com.tuotiansudai.message.repository.model.UserMessageModel;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.point.repository.model.PointTask;
import com.tuotiansudai.point.service.PointTaskService;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Component
public class BankAccountRegisteredEventMessageConsumer implements MessageConsumer {

    private static Logger logger = LoggerFactory.getLogger(BankAccountRegisteredEventMessageConsumer.class);

    private List<String> JSON_KEYS = Lists.newArrayList("mobilePhone", "identityCode", "realName", "accountNo", "userName", "orderDate", "orderNo");

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MessageMapper messageMapper;

    @Override
    public MessageQueue queue() {
        return MessageQueue.CertificationSuccess_EventMessage;
    }

    @Override
    public void consume(String message) {
        logger.info("[MQ] receive message: {}: {}.", this.queue(), message);

        if (Strings.isNullOrEmpty(message)) {
            logger.error("[MQ] CertificationSuccess_EventMessage message is empty");
            return;
        }


        mqWrapperClient.sendMessage(MessageQueue.EventMessage, new EventMessage(MessageEventType.REGISTER_ACCOUNT_SUCCESS,
                Lists.newArrayList(dto.getLoginName()),
                MessageEventType.REGISTER_ACCOUNT_SUCCESS.getTitleTemplate(),
                MessageFormat.format(MessageEventType.REGISTER_ACCOUNT_SUCCESS.getContentTemplate(), dto.getUserName()),
                null
        ));


        MessageModel messageModel = messageMapper.findActiveByEventType(MessageEventType.REGISTER_ACCOUNT_SUCCESS);
        if (messageModel == null) {
            logger.error(MessageFormat.format("[CertificationSuccess_EventMessage] message({0}) event type not found", message));
            return;
        }

        UserMessageModel userMessageModel = new UserMessageModel(messageModel.getId(), , eventMessage.getTitle(), eventMessage.getContent(), new Date());
        userMessageModel.setBusinessId(entry.getKey());
        userMessageMapper.create(userMessageModel);



        try{
            HashMap<String, String> map = new Gson().fromJson(message, new TypeToken<HashMap<String, String>>() {
            }.getType());

            if (Sets.difference(map.keySet(), Sets.newHashSet(JSON_KEYS)).isEmpty()) {
                UserModel userModel = userMapper.findByMobile(map.get("mobilePhone"));
            }else {
                logger.error("[MQ] message is invalid {}", message);
            }

        }catch (Exception e){
            logger.error(MessageFormat.format("[MQ] consume message error, message: {0}", message), e);
        }
    }
}
