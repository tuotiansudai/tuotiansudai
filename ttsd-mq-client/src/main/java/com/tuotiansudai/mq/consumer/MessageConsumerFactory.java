package com.tuotiansudai.mq.consumer;

import com.tuotiansudai.mq.client.MQClient;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

public class MessageConsumerFactory {
    @Autowired
    private MQClient mqClient;

    private Set<MessageConsumer> messageConsumers;

    @Autowired(required = false)
    public void setMessageConsumers(Set<MessageConsumer> messageConsumers) {
        this.messageConsumers = messageConsumers;
    }

    public void start() {
        if (messageConsumers != null) {
            messageConsumers.forEach(consumer ->
                    new Thread(() ->
                            mqClient.subscribe(consumer.queue(), consumer::consume),
                            "Consumer-" + consumer.queue().getQueueName()
                    ).start()
            );
        }
    }

}
