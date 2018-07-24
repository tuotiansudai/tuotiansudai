package com.tuotiansudai.mq.consumer.amount;

import com.tuotiansudai.message.BankSystemBillMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.mq.consumer.amount.service.BankSystemBillService;
import com.tuotiansudai.util.JsonConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class BankSystemBillMessageConsumer implements MessageConsumer {

    static Logger logger = LoggerFactory.getLogger(BankSystemBillMessageConsumer.class);

    @Autowired
    private BankSystemBillService bankSystemBillService;

    @Override
    public MessageQueue queue() {
        return MessageQueue.BankSystemBill;
    }

    @Override
    public void consume(String message) {
        logger.info("[BankSystemBill] receive message: {}: {}.", this.queue(), message);

        try {
            BankSystemBillMessage sbm = JsonConverter.readValue(message, BankSystemBillMessage.class);
            bankSystemBillService.systemBillProcess(sbm);
        } catch (Exception e) {
            logger.error(MessageFormat.format("[MQ] system bill consumer fail, message:{0}", message), e);
        }
        logger.info("[SystemBill] receive message: {}: {} done.", this.queue(), message);
    }

}