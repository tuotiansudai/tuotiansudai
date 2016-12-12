package com.tuotiansudai.mq.consumer;

import com.tuotiansudai.mq.client.MQClient;
import org.springframework.beans.factory.annotation.Autowired;
import sun.misc.Signal;
import sun.misc.SignalHandler;

import java.util.Set;

public class MessageConsumerFactory implements SignalHandler {
    private final String STOP_SIGNAL = "TERM";
    private MQClient mqClient;
    private Set<MessageConsumer> messageConsumers;

    @Autowired
    public void setMqClient(MQClient mqClient) {
        this.mqClient = mqClient;
    }

    @Autowired(required = false)
    public void setMessageConsumers(Set<MessageConsumer> messageConsumers) {
        this.messageConsumers = messageConsumers;
    }

    public void start() {
        registerSignalHandler();
        if (messageConsumers != null) {
            messageConsumers.forEach(consumer ->
                    new Thread(() ->
                            mqClient.subscribe(consumer.queue(), consumer::consume),
                            "Consumer-" + consumer.queue().getQueueName()
                    ).start()
            );
        }
    }

    private void registerSignalHandler() {
        Signal.handle(new Signal(STOP_SIGNAL), this);
    }

    @Override
    public void handle(Signal signal) {
        if (STOP_SIGNAL.equals(signal.getName())) {
            mqClient.stopSubscribe();
        }
    }
}
