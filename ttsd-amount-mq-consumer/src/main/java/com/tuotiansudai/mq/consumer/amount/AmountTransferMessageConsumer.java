package com.tuotiansudai.mq.consumer.amount;

import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.exception.AmountTransferException;
import com.tuotiansudai.message.AmountTransferMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.mq.consumer.amount.service.AmountTransferService;
import com.tuotiansudai.util.JsonConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.MessageFormat;

@Component
public class AmountTransferMessageConsumer implements MessageConsumer {

    static Logger logger = LoggerFactory.getLogger(AmountTransferMessageConsumer.class);

    @Autowired
    private AmountTransferService amountTransferService;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Override
    public MessageQueue queue() {
        return MessageQueue.AmountTransfer;
    }

    @Override
    public void consume(String message) {
        logger.info("[AmountTransfer] receive message: {}: {}.", this.queue(), message);

        try {
            AmountTransferMessage atm = JsonConverter.readValue(message, AmountTransferMessage.class);
            atm.addTryTimes();
            try {
                amountTransferService.amountTransferProcess(atm);
            } catch (AmountTransferException e) {
                // 如果消息消费失败了，则重试1次：
                if (atm.getTryTimes() <= 1) {
                    logger.info(MessageFormat.format("[MQ] amount transfer consumer fail, will retry soon. message:{0}", message), e);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ie) {
                        logger.info(MessageFormat.format("[MQ] amount transfer consumer fail, message:{0}", message), ie);
                    }
                    mqWrapperClient.sendMessage(MessageQueue.AmountTransfer, atm);
                } else {
                    logger.error(MessageFormat.format("[MQ] amount transfer consumer fail after try {0} times, message:{0}", atm.getTryTimes(), message), e);
                }
            }
        } catch (IOException e) {
            logger.error(MessageFormat.format("[MQ] amount transfer consumer read message fail, message:{0}", message), e);
        }
        logger.info("[AmountTransfer] receive message: {}: {} done.", this.queue(), message);
    }
}
