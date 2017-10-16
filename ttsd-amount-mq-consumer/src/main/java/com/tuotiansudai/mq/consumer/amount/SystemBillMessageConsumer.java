package com.tuotiansudai.mq.consumer.amount;

import com.tuotiansudai.message.SystemBillMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.mq.consumer.amount.service.SystemBillService;
import com.tuotiansudai.util.JsonConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class SystemBillMessageConsumer implements MessageConsumer {

    static Logger logger = LoggerFactory.getLogger(SystemBillMessageConsumer.class);

    @Autowired
    private SystemBillService systemBillService;

    @Override
    public MessageQueue queue() {
        return MessageQueue.SystemBill;
    }

    @Override
    public void consume(String message) {
        logger.info("[SystemBill] receive message: {}: {}.", this.queue(), message);

        try {
            SystemBillMessage sbm = JsonConverter.readValue(message, SystemBillMessage.class);
            systemBillService.systemBillProcess(sbm);
        } catch (Exception e) {
            logger.error(MessageFormat.format("[MQ] system bill consumer fail, message:{0}", message), e);
        }
        logger.info("[SystemBill] receive message: {}: {} done.", this.queue(), message);
    }
}