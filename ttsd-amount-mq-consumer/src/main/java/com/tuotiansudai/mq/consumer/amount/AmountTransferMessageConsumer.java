package com.tuotiansudai.mq.consumer.amount;

import com.tuotiansudai.message.AmountTransferMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.mq.consumer.amount.service.AmountTransferService;
import com.tuotiansudai.util.JsonConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class AmountTransferMessageConsumer implements MessageConsumer {

    private static Logger logger = LoggerFactory.getLogger(AmountTransferMessageConsumer.class);

    private final AmountTransferService amountTransferService;

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
            AmountTransferMessage amountTransferMessage = JsonConverter.readValue(message, AmountTransferMessage.class);
            amountTransferService.amountTransferProcess(amountTransferMessage);
        } catch (Exception e) {
            logger.error(MessageFormat.format("[MQ] amount transfer consumer fail, message:{0}", message), e);
        }
        logger.info("[AmountTransfer] receive message: {}: {} done.", this.queue(), message);
    }
}
