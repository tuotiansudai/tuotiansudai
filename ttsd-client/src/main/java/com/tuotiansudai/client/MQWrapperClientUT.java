package com.tuotiansudai.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tuotiansudai.mq.client.MQProducer;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.client.model.MessageTopic;
import com.tuotiansudai.util.JsonConverter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class MQWrapperClientUT implements MQWrapperClient {

    private static Logger logger = Logger.getLogger(MQWrapperClient.class);

    @Autowired
    private MQProducer mqProducer;

    public void publishMessage(final MessageTopic topic, Object message) throws JsonProcessingException {
        String messageString = JsonConverter.writeValueAsString(message);
        mqProducer.publishMessage(topic, messageString);
    }

    public void sendMessage(final MessageQueue queue, final String message) {
        mqProducer.sendMessage(queue, message);
    }

    public void sendMessage(final MessageQueue queue, final Object message) {
        try {
            mqProducer.sendMessage(queue, JsonConverter.writeValueAsString(message));
        } catch (JsonProcessingException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }

}
