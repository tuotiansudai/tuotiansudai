package com.tuotiansudai.mq.consumer;

import com.tuotiansudai.mq.client.model.MessageQueue;

public interface MessageConsumer {
    MessageQueue queue();

    void consume(String message);
}
