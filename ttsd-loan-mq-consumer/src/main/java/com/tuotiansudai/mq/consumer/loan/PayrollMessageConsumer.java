package com.tuotiansudai.mq.consumer.loan;

import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PayrollMessageConsumer implements MessageConsumer {

    private static Logger logger = LoggerFactory.getLogger(PayrollMessageConsumer.class);

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Override
    public MessageQueue queue() {
        return MessageQueue.Payroll;
    }

    @Override
    public void consume(String message) {
        logger.info("[payroll] trigger payroll job, prepare call pay-wrapper");
        try {
            long payrollId = Long.parseLong(message);
            logger.info("[payroll] trigger payroll job, payrollId: " + message);
            BaseDto<BaseDataDto> dto = payWrapperClient.payroll(payrollId);
            if (!dto.getData().getStatus()) {
                logger.error("[payroll] execute payroll job failed, payrollId: " + message);
            } else {
                logger.info("[payroll] execute payroll job success, payrollId: " + message);
            }
        } catch (Exception e) {
            logger.error("[payroll] execute payroll job failed, payrollId:" + message, e);
        }
    }
}
