package com.tuotiansudai.mq.consumer.loan;

import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.repository.mapper.PayrollDetailMapper;
import com.tuotiansudai.repository.model.PayrollDetailModel;
import com.tuotiansudai.repository.model.PayrollPayStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PayrollConfirmFailMessageConsumer implements MessageConsumer {

    private static Logger logger = LoggerFactory.getLogger(PayrollConfirmFailMessageConsumer.class);

    @Autowired
    private PayrollDetailMapper payrollDetailMapper;

    @Override
    public MessageQueue queue() {
        return MessageQueue.PayrollConfirmFail;
    }

    @Override
    public void consume(String message) {
        logger.info("[payroll] trigger PayrollConfirmFail job, prepare call pay-wrapper");
        try {
            long payrollDetailId = Long.parseLong(message);
            logger.info("[payroll] trigger PayrollConfirmFail job, payrollDetailId: " + message);
            PayrollDetailModel payrollDetailModel = payrollDetailMapper.findById(payrollDetailId);
            if (payrollDetailModel.getStatus() == PayrollPayStatus.PAYING) {
                payrollDetailMapper.updateStatus(payrollDetailId, PayrollPayStatus.FAIL);
            }
            logger.info("[payroll] execute PayrollConfirmFail job success, payrollDetailId: " + message);
        } catch (Exception e) {
            logger.error("[payroll] execute PayrollConfirmFail job failed, payrollDetailId:" + message, e);
        }
    }
}
