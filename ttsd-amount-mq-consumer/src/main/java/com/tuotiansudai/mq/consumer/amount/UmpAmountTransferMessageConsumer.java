package com.tuotiansudai.mq.consumer.amount;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.tuotiansudai.message.AmountTransferMessage;
import com.tuotiansudai.message.UmpAmountTransferMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.mq.consumer.amount.service.AmountTransferService;
import com.tuotiansudai.mq.consumer.amount.service.UmpAmountTransferService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.List;

@Component
public class UmpAmountTransferMessageConsumer implements MessageConsumer {

    private static Logger logger = LoggerFactory.getLogger(UmpAmountTransferMessageConsumer.class);

    private final UmpAmountTransferService umpAmountTransferService;

    private final Gson gson = new GsonBuilder().create();

    @Autowired
    public UmpAmountTransferMessageConsumer(UmpAmountTransferService umpAmountTransferService) {
        this.umpAmountTransferService = umpAmountTransferService;
    }

    @Override
    public MessageQueue queue() {
        return MessageQueue.UmpAmountTransfer;
    }

    @Override
    public void consume(String message) {
        logger.info("[UmpAmountTransfer] receive message: {}: {}.", this.queue(), message);

        try {
            List<UmpAmountTransferMessage> messages = gson.fromJson(message, new TypeToken<List<UmpAmountTransferMessage>>(){}.getType());
            umpAmountTransferService.process(messages);
            logger.info("[UmpAmountTransfer] receive message: {}: {} done.", this.queue(), message);
        } catch (Exception e) {
            logger.error(MessageFormat.format("[MQ] amount transfer consumer fail, message:{0}", message), e);
        }
    }
}
