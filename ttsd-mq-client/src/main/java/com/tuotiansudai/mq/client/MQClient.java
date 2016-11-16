package com.tuotiansudai.mq.client;

import com.tuotiansudai.mq.client.model.MessageTopic;
import com.tuotiansudai.mq.client.model.MessageTopicQueue;

import java.util.function.Consumer;

public interface MQClient {


    void publishMessage(final MessageTopic topic, final String message);

    void subscribe(final MessageTopicQueue queue, Consumer<com.tuotiansudai.mq.client.model.Message> consumer);
}
