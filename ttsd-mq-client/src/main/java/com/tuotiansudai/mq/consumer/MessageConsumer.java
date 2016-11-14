package com.tuotiansudai.mq.consumer;

import com.tuotiansudai.mq.client.model.Message;
import com.tuotiansudai.mq.client.model.MessageTopicQueue;

public interface MessageConsumer {
    MessageTopicQueue queue();

    void consume(Message message);
}
