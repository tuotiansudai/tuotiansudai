package com.tuotiansudai.mq.consumer;

import com.tuotiansudai.mq.client.MQConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import sun.misc.Signal;
import sun.misc.SignalHandler;

import java.util.Set;

public class MessageConsumerFactory implements SignalHandler {
    private final String STOP_SIGNAL = "TERM";
    private final MQConsumer mqConsumer;
    private Set<MessageConsumer> messageConsumers;

    @Autowired
    public void setMessageConsumers(Set<MessageConsumer> messageConsumers) {
        this.messageConsumers = messageConsumers;
    }

    @Autowired
    public MessageConsumerFactory(MQConsumer mqConsumer) {
        this.mqConsumer = mqConsumer;
    }

    public void start() {
        registerSignalHandler();
        if (messageConsumers != null) {
            messageConsumers.forEach(consumer ->
                    new Thread(() ->
                            mqConsumer.subscribe(consumer.queue(), consumer::consume),
                            "Consumer-" + consumer.queue().getQueueName()
                    ).start()
            );
        }
    }

    @Override
    public void handle(Signal signal) {
        if (STOP_SIGNAL.equals(signal.getName())) {
            mqConsumer.stopSubscribe();
        }
    }

    private void registerSignalHandler() {
        Signal.handle(new Signal(STOP_SIGNAL), this);
    }

}
