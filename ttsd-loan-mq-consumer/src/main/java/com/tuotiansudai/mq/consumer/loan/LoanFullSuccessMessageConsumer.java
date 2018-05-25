package com.tuotiansudai.mq.consumer.loan;

import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LoanFullSuccessMessageConsumer implements MessageConsumer {

    private static Logger logger = LoggerFactory.getLogger(LoanFullSuccessMessageConsumer.class);

    private final static String ALREADY_LOAN_OUT_RETURN_CODE = "0001";

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Override
    public MessageQueue queue() {
        return MessageQueue.LoanFull_Success;
    }

    @Override
    public void consume(String message) {
        logger.info("trigger auto loan out after raising complete job, prepare do job");
        try {
            long loanId = Long.parseLong(message);
            logger.info("trigger auto loan out after raising complete job, loanId : " + String.valueOf(loanId));

            BaseDto<PayDataDto> dto = payWrapperClient.autoLoanOutAfterRaisingComplete(loanId);
            if (!dto.getData().getStatus() && !ALREADY_LOAN_OUT_RETURN_CODE.equals(dto.getData().getCode())) {
                logger.info("loan has already been outed.[" + loanId + "]");
            }

        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }

    }
}
