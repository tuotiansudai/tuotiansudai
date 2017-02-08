package com.tuotiansudai.mq.consumer.email;

import com.google.common.base.Strings;
import com.tuotiansudai.message.EMailMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.util.JsonConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class EMailMessageConsumer implements MessageConsumer {

    private static Logger logger = LoggerFactory.getLogger(EMailMessageConsumer.class);

    private final SendCloudClient sendCloudClient;

    @Autowired
    public EMailMessageConsumer(SendCloudClient sendCloudClient) {
        this.sendCloudClient = sendCloudClient;
    }

    @Override
    public MessageQueue queue() {
        return MessageQueue.EMailMessage;
    }

    @Override
    public void consume(String message) {
        if (Strings.isNullOrEmpty(message)) {
            logger.error("[EMailMessageConsumer] message is empty");
            return;
        }

        try {
            EMailMessage eMailMessage = JsonConverter.readValue(message, EMailMessage.class);
            if (eMailMessage == null || Strings.isNullOrEmpty(eMailMessage.getTitle())
                    || Strings.isNullOrEmpty(eMailMessage.getContent())
                    || eMailMessage.getAddresses() == null
                    || eMailMessage.getAddresses().size() == 0) {
                logger.error(MessageFormat.format("[EMailMessageConsumer] message({0}) is invalid", message));
                return;
            }
            sendCloudClient.sendMailBySendCloud(eMailMessage.getAddresses(), eMailMessage.getTitle(), eMailMessage.getContent());
        } catch (Exception e) {
            logger.error(MessageFormat.format("[EMailMessageConsumer] {0}", message), e);
        }
    }
}
