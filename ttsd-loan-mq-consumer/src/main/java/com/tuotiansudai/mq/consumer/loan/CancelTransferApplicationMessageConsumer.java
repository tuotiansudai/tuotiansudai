package com.tuotiansudai.mq.consumer.loan;

import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.transfer.service.InvestTransferService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CancelTransferApplicationMessageConsumer implements MessageConsumer {

    static Logger logger = LoggerFactory.getLogger(CancelTransferApplicationMessageConsumer.class);

    @Autowired
    private InvestTransferService investTransferService;

    @Override
    public MessageQueue queue() {
        return MessageQueue.CancelTransferApplication;
    }

    @Override
    public void consume(String message) {
        long id = Long.parseLong(message);
        logger.info("TransferApplyAutoCancelJob===========in, id = " + id);
        investTransferService.cancelTransferApplication(id);
        logger.info("TransferApplyAutoCancelJob===========out, id = " + id);
    }
}
