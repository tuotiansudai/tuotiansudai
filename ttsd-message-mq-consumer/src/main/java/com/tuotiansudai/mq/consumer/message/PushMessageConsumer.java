package com.tuotiansudai.mq.consumer.message;

import com.google.common.base.Strings;
import com.tuotiansudai.message.PushMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.client.PushClient;
import com.tuotiansudai.util.JsonConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class PushMessageConsumer implements MessageConsumer {

    private static Logger logger = LoggerFactory.getLogger(PushMessageConsumer.class);

    @Autowired
    private PushClient pushClient;

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
            pushClient.sendJPush(JsonConverter.readValue(message, PushMessage.class));
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }
}
