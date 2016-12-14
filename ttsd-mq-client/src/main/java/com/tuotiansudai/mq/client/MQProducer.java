package com.tuotiansudai.mq.client;

import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.client.model.MessageTopic;

public interface MQProducer {
    void publishMessage(final MessageTopic topic, final String message);

    void sendMessage(final MessageQueue queue, final String message);
}
