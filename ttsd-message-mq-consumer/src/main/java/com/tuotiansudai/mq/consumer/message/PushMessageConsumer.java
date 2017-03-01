package com.tuotiansudai.mq.consumer.message;

import com.google.common.base.Strings;
import com.tuotiansudai.client.MiPushClient;
import com.tuotiansudai.message.PushMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.util.JsonConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class PushMessageConsumer implements MessageConsumer {

    private static Logger logger = LoggerFactory.getLogger(PushMessageConsumer.class);

    private final MiPushClient miPushClient;

    @Autowired
    public PushMessageConsumer(MiPushClient miPushClient) {
        this.miPushClient = miPushClient;
    }

    @Override
    public MessageQueue queue() {
        return MessageQueue.PushMessage;
    }

    @Override
    public void consume(String message) {
        if (Strings.isNullOrEmpty(message)) {
            logger.error("[PushMessageConsumer] message is empty");
            return;
        }

        try {
            PushMessage pushMessage = JsonConverter.readValue(message, PushMessage.class);
            if (pushMessage == null || pushMessage.getPushType() == null || pushMessage.getPushSource() == null || Strings.isNullOrEmpty(pushMessage.getContent())) {
                logger.error(MessageFormat.format("[PushMessageConsumer] message ({0}) is invalid", message));
                return;
            }

            miPushClient.sendPushMessage(pushMessage);
        } catch (Exception e) {
            logger.error(MessageFormat.format("[PushMessageConsumer] {0}", message), e);
        }
    }
}
