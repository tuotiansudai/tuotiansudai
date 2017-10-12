package com.tuotiansudai.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.client.model.MessageTopic;

public interface MQWrapperClient {

    void publishMessage(final MessageTopic topic, Object message) throws JsonProcessingException;

    void sendMessage(final MessageQueue queue, final String message);

    void sendMessage(final MessageQueue queue, final Object message);

}
