package com.tuotiansudai.mq.client;

import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.client.model.MessageTopic;

import java.util.function.Consumer;

public interface MQClient {
    int TIME_SLICE_SECONDS = 10;

    void publishMessage(final MessageTopic topic, final String message);

    void sendMessage(final MessageQueue queue, final String message);

    void subscribe(final MessageQueue queue, final Consumer<String> consumer);

    void stopSubscribe();
}
