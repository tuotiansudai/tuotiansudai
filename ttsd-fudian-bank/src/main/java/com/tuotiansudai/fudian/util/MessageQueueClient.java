package com.tuotiansudai.fudian.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tuotiansudai.mq.client.MQProducer;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.client.model.MessageTopic;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Component
public class MessageQueueClient {

    private static Logger logger = Logger.getLogger(MessageQueueClient.class);

    private final MQProducer mqProducer;

    private final Gson gson = new GsonBuilder().create();

    @Autowired
    public MessageQueueClient(MQProducer mqProducer) {
        this.mqProducer = mqProducer;
    }

    public void publishMessage(final MessageTopic topic, Object message) {
        String messageString = gson.toJson(message);
        runAfterCommit(() -> mqProducer.publishMessage(topic, messageString));
    }

    public void sendMessage(final MessageQueue queue, final String message) {
        runAfterCommit(() -> mqProducer.sendMessage(queue, message));
    }

    public void sendMessage(final MessageQueue queue, final Object message) {
        runAfterCommit(() -> {
            try {
                mqProducer.sendMessage(queue, gson.toJson(message));
            } catch (Exception e) {
                logger.error(e.getLocalizedMessage(), e);
            }
        });
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
