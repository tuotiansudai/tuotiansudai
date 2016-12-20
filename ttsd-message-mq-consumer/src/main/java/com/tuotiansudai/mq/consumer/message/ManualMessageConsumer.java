package com.tuotiansudai.mq.consumer.message;

import com.google.common.base.Strings;
import com.tuotiansudai.message.EventMessage;
import com.tuotiansudai.message.ManualMessage;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class ManualMessageConsumer implements MessageConsumer {

    private static Logger logger = LoggerFactory.getLogger(ManualMessageConsumer.class);

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
            ManualMessage manualMessage = JsonConverter.readValue(message, ManualMessage.class);

            for (long messageId : manualMessage.getMessageIds()) {
                MessageModel messageModel = messageMapper.findById(messageId);
                UserMessageModel userMessageModel = new UserMessageModel(messageModel.getId(), manualMessage.getLoginName(), messageModel.getTitle(), messageModel.getTitle(), messageModel.getTemplate(), messageModel.getActivatedTime());
                userMessageMapper.create(userMessageModel);
            }
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }
}
