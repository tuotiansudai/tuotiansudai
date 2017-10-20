package com.tuotiansudai.mq.consumer.message;

import com.google.common.base.Strings;
import com.tuotiansudai.enums.MessageEventType;
import com.tuotiansudai.message.EventMessage;
import com.tuotiansudai.message.repository.mapper.MessageMapper;
import com.tuotiansudai.message.repository.mapper.UserMessageMapper;
import com.tuotiansudai.message.repository.model.MessageModel;
import com.tuotiansudai.message.repository.model.UserMessageModel;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.util.JsonConverter;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Date;
import java.util.Map;

@Component
public class EventMessageConsumer implements MessageConsumer {

    private static Logger logger = LoggerFactory.getLogger(EventMessageConsumer.class);

    private final MessageMapper messageMapper;

    private final UserMessageMapper userMessageMapper;

    @Autowired
    public EventMessageConsumer(MessageMapper messageMapper, UserMessageMapper userMessageMapper) {
        this.messageMapper = messageMapper;
        this.userMessageMapper = userMessageMapper;
    }

    @Override
    public MessageQueue queue() {
        return MessageQueue.EventMessage;
    }

    @Override
    public void consume(String message) {
        if (Strings.isNullOrEmpty(message)) {
            logger.error("[EventMessageConsumer] message is empty");
            return;
        }

        try {
            EventMessage eventMessage = JsonConverter.readValue(message, EventMessage.class);
            if (eventMessage == null || Strings.isNullOrEmpty(eventMessage.getTitle()) || Strings.isNullOrEmpty(eventMessage.getContent()) || eventMessage.getEventType() == null
                    || (CollectionUtils.isEmpty(eventMessage.getLoginNames()) && eventMessage.getEventType() != MessageEventType.LOAN_OUT_SUCCESS)) {
                logger.error(MessageFormat.format("[EventMessageConsumer] message({0}) is invalid", message));
                return;
            }

            MessageModel messageModel = messageMapper.findActiveByEventType(eventMessage.getEventType());
            if (messageModel == null) {
                logger.error(MessageFormat.format("[EventMessageConsumer] message({0}) event type not found", message));
                return;
            }

            if (eventMessage.getEventType() == MessageEventType.LOAN_OUT_SUCCESS){
                for (Map.Entry<Long, String> entry : eventMessage.getBusinessIdLoginNames().entrySet()){
                    UserMessageModel userMessageModel = new UserMessageModel(messageModel.getId(), entry.getValue(), eventMessage.getTitle(), eventMessage.getContent(), new Date());
                    userMessageModel.setBusinessId(entry.getKey());
                    userMessageMapper.create(userMessageModel);
                }
            }else{
                for (String loginName : eventMessage.getLoginNames()) {
                    UserMessageModel userMessageModel = new UserMessageModel(messageModel.getId(), loginName, eventMessage.getTitle(), eventMessage.getContent(), new Date());
                    userMessageModel.setBusinessId(eventMessage.getBusinessId() != null ? eventMessage.getBusinessId() : null);
                    userMessageMapper.create(userMessageModel);
                }
            }

        } catch (Exception e) {
            logger.error(MessageFormat.format("[EventMessageConsumer] {0}", message), e);
        }
    }
}
