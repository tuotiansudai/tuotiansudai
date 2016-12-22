package com.tuotiansudai.mq.consumer.message;

import com.google.common.base.Strings;
import com.tuotiansudai.message.EventMessage;
import com.tuotiansudai.message.repository.mapper.MessageMapper;
import com.tuotiansudai.message.repository.mapper.UserMessageMapper;
import com.tuotiansudai.message.repository.model.MessageModel;
import com.tuotiansudai.message.repository.model.UserMessageModel;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.util.JsonConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

@Component
public class EventMessageConsumer implements MessageConsumer {

    private static Logger logger = LoggerFactory.getLogger(EventMessageConsumer.class);

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private UserMessageMapper userMessageMapper;

    @Override
    public MessageQueue queue() {
        return MessageQueue.PushMessage;
    }

    @Override
    public void consume(String message) {
        if (Strings.isNullOrEmpty(message)) {
            return;
        }

        try {
            EventMessage eventMessage = JsonConverter.readValue(message, EventMessage.class);
            MessageModel messageModel = messageMapper.findActiveByEventType(eventMessage.getEventType());
            for (String loginName : eventMessage.getLoginNames()) {
                UserMessageModel userMessageModel = new UserMessageModel(messageModel.getId(), loginName, eventMessage.getTitle(), eventMessage.getContent(), new Date());
                userMessageModel.setBusinessId(eventMessage.getBusinessId() != null ? eventMessage.getBusinessId() : null);
                userMessageMapper.create(userMessageModel);
            }
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }
}
