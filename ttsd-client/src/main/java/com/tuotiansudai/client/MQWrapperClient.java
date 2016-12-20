package com.tuotiansudai.client;

import com.tuotiansudai.mq.client.MQProducer;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.client.model.MessageTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Component
public class MQWrapperClient {

    @Autowired
    private MQProducer mqProducer;

    public void publishMessage(final MessageTopic topic, final String message) {
        runAfterCommit(() -> mqProducer.publishMessage(topic, message));
    }

    public void sendMessage(final MessageQueue queue, final String message) {
        runAfterCommit(() -> mqProducer.sendMessage(queue, message));
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
