package com.tuotiansudai.mq.consumer.message;

import com.google.common.base.Strings;
import com.tuotiansudai.message.ManualMessage;
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

import java.io.IOException;
import java.text.MessageFormat;

@Component
public class ManualMessageConsumer implements MessageConsumer {

    private static Logger logger = LoggerFactory.getLogger(ManualMessageConsumer.class);

    private final MessageMapper messageMapper;

    private final UserMessageMapper userMessageMapper;

    @Autowired
    public ManualMessageConsumer(MessageMapper messageMapper, UserMessageMapper userMessageMapper) {
        this.messageMapper = messageMapper;
        this.userMessageMapper = userMessageMapper;
    }

    @Override
    public MessageQueue queue() {
        return MessageQueue.ManualMessage;
    }

    @Override
    public void consume(String message) {
        if (Strings.isNullOrEmpty(message)) {
            logger.error("[ManualMessageConsumer] message is empty");
            return;
        }

        try {
            ManualMessage manualMessage = JsonConverter.readValue(message, ManualMessage.class);

            if (manualMessage == null || CollectionUtils.isEmpty(manualMessage.getMessageIds())) {
                logger.error(MessageFormat.format("[ManualMessageConsumer] message({0}) is invalid", message));
                return;
            }

            for (long messageId : manualMessage.getMessageIds()) {
                MessageModel messageModel = messageMapper.findActiveById(messageId);
                if (messageModel == null) {
                    logger.error(MessageFormat.format("[ManualMessageConsumer] message({0}) not found", message));
                    continue;
                }
                if (userMessageMapper.countByLoginNameAndMessageId(manualMessage.getLoginName(), messageId) == 0) {
                    UserMessageModel userMessageModel = new UserMessageModel(messageModel.getId(), manualMessage.getLoginName(), messageModel.getTitle(), messageModel.getTemplate(), messageModel.getActivatedTime());
                    userMessageMapper.create(userMessageModel);
                }
            }
        } catch (Exception e) {
            logger.error(MessageFormat.format("[ManualMessageConsumer] {0}", message), e);
        }
    }
}
