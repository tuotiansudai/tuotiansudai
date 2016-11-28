package com.tuotiansudai.client;

import com.tuotiansudai.mq.client.MQClient;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.client.model.MessageTopic;
import com.tuotiansudai.mq.client.model.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.function.Consumer;

@Component
public class MQWrapperClient {

    @Autowired
    private MQClient mqClient;

    public void publishMessage(final MessageTopic topic, final String message) {
        runAfterCommit(() -> mqClient.publishMessage(topic, message));
    }

    public void sendMessage(final MessageQueue queue, final String message) {
        runAfterCommit(() -> mqClient.sendMessage(queue, message));
    }

    public void subscribe(final Queue queue, Consumer<String> consumer) {
        mqClient.subscribe(queue, consumer);
    }

    private void runAfterCommit(Runnable runnable) {
        if (runnable == null) {
            return;
        }
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
                @Override
                public void afterCommit() {
                    runnable.run();
                }
            });
        } else {
            runnable.run();
        }
    }
}
