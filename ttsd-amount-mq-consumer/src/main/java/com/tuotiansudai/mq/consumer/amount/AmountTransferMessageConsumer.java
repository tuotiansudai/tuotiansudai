package com.tuotiansudai.mq.consumer.amount;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.tuotiansudai.message.AmountTransferMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.mq.consumer.amount.service.AmountTransferService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.List;

@Component
public class AmountTransferMessageConsumer implements MessageConsumer {

    private static Logger logger = LoggerFactory.getLogger(AmountTransferMessageConsumer.class);

    private final AmountTransferService amountTransferService;

    private final Gson gson = new GsonBuilder().create();

    @Autowired
    public AmountTransferMessageConsumer(AmountTransferService amountTransferService) {
        this.amountTransferService = amountTransferService;
    }

    @Override
    public MessageQueue queue() {
        return MessageQueue.AmountTransfer;
    }

    @Override
    public void consume(String message) {
        logger.info("[AmountTransfer] receive message: {}: {}.", this.queue(), message);

        try {
            List<AmountTransferMessage> messages = gson.fromJson(message, new TypeToken<List<AmountTransferMessage>>(){}.getType());
            amountTransferService.process(messages);
            logger.info("[AmountTransfer] receive message: {}: {} done.", this.queue(), message);
        } catch (Exception e) {
            logger.error(MessageFormat.format("[MQ] amount transfer consumer fail, message:{0}", message), e);
        }
    }
}
