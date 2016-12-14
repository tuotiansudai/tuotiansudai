package com.tuotiansudai.mq.consumer;

import com.tuotiansudai.mq.client.MQConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

import java.util.Set;
import java.util.stream.Collectors;

public class MessageConsumerFactory implements ApplicationListener<ContextClosedEvent> {
    private final MQConsumer mqConsumer;
    private Set<MessageConsumer> messageConsumers;
    private Set<Thread> consumerThreads;

    @Autowired
    public void setMessageConsumers(Set<MessageConsumer> messageConsumers) {
        this.messageConsumers = messageConsumers;
    }

    @Autowired
    public MessageConsumerFactory(MQConsumer mqConsumer) {
        this.mqConsumer = mqConsumer;
    }

    public void start() {
        if (messageConsumers == null) {
            return;
        }
        consumerThreads = messageConsumers.stream()
                .map(this::startSubscribe)
                .collect(Collectors.toSet());
    }

    private Thread startSubscribe(MessageConsumer consumer) {
        Thread thread = new Thread(() -> mqConsumer.subscribe(consumer.queue(), consumer::consume), "Consumer-" + consumer.queue().getQueueName());
        thread.start();
        return thread;
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        mqConsumer.stopSubscribe();
        for (Thread consumerThread : consumerThreads) {
            try {
                consumerThread.join();
            } catch (InterruptedException ignored) {
            }
        }
    }
}
