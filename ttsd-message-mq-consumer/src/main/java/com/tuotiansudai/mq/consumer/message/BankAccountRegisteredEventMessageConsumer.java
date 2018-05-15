package com.tuotiansudai.mq.consumer.message;


import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tuotiansudai.enums.MessageEventType;
import com.tuotiansudai.message.repository.mapper.MessageMapper;
import com.tuotiansudai.message.repository.mapper.UserMessageMapper;
import com.tuotiansudai.message.repository.model.MessageModel;
import com.tuotiansudai.message.repository.model.UserMessageModel;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
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

    private List<String> JSON_KEYS = Lists.newArrayList("loginName", "mobile", "identityCode", "realName", "accountNo", "userName", "orderDate", "orderNo");

    @Autowired
    private UserMessageMapper userMessageMapper;

    @Autowired
    private MessageMapper messageMapper;

    @Override
    public MessageQueue queue() {
        return MessageQueue.RegisterBankAccount_EventMessage;
    }

    @Override
    public void consume(String message) {
        logger.info("[MQ] receive message: {}: {}.", this.queue(), message);

        if (Strings.isNullOrEmpty(message)) {
            logger.error("[MQ] RegisterBankAccount_EventMessage message is empty");
            return;
        }

        try{
            HashMap<String, String> map = new Gson().fromJson(message, new TypeToken<HashMap<String, String>>() {
            }.getType());
            if (Sets.difference(map.keySet(), Sets.newHashSet(JSON_KEYS)).isEmpty()) {
                MessageModel messageModel = messageMapper.findActiveByEventType(MessageEventType.REGISTER_ACCOUNT_SUCCESS);
                UserMessageModel userMessageModel = new UserMessageModel(messageModel.getId(),
                        map.get("loginName"),
                        MessageEventType.REGISTER_ACCOUNT_SUCCESS.getTitleTemplate(),
                        MessageFormat.format(MessageEventType.REGISTER_ACCOUNT_SUCCESS.getContentTemplate(), map.get("realName")), new Date());
                userMessageMapper.create(userMessageModel);

            }else {
                logger.error("[MQ] message is invalid {}", message);
            }

        }catch (Exception e){
            logger.error(MessageFormat.format("[MQ] consume message error, message: {0}", message), e);
        }
    }
}
