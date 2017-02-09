package com.tuotiansudai.mq.consumer.loan;

import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.repository.model.LoanStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class LoanStopRaisingMessageConsumer implements MessageConsumer {

    private static Logger logger = LoggerFactory.getLogger(LoanStopRaisingMessageConsumer.class);

    @Autowired
    private LoanMapper loanMapper;

    @Override
    public MessageQueue queue() {
        return MessageQueue.LoanStopRaising;
    }

    @Override
    public void consume(String message) {
        logger.info("trigger DeadlineFundraisingJob");

        long loanId = Long.parseLong(message);

        LoanModel loanModel = loanMapper.findById(loanId);

        logger.info("loanId = " + loanId);
        logger.info("status = " + loanModel.getStatus());
        logger.info("fundraisingEndTime = " + loanModel.getFundraisingEndTime());

        if (loanModel.getStatus() == LoanStatus.RAISING && loanModel.getFundraisingEndTime().before(new Date())) {
            loanMapper.updateStatus(loanModel.getId(), LoanStatus.RECHECK);
        }
    }
}
