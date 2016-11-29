package com.tuotiansudai.mq.client;

import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.client.model.MessageTopic;
import com.tuotiansudai.mq.client.model.Queue;

import java.util.function.Consumer;

public interface MQClient {

    void publishMessage(final MessageTopic topic, final String message);

    void sendMessage(final MessageQueue queue, final String message);

    void subscribe(final Queue queue, Consumer<String> consumer);
}
