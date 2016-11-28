package com.tuotiansudai.mq.consumer;

import com.tuotiansudai.mq.client.model.Queue;

public interface MessageConsumer {
    Queue queue();

    void consume(String message);
}
