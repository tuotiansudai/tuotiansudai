package com.tuotiansudai.mq.consumer;

import com.tuotiansudai.mq.client.model.Message;
import com.tuotiansudai.mq.client.model.MessageTopicQueue;
import com.tuotiansudai.mq.client.model.Queue;

public interface MessageConsumer {
    Queue queue();

    void consume(Message message);
}
