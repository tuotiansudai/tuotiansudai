package com.tuotiansudai.console.service;

import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.mq.client.model.MessageQueue;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConsolePayrollService {

    static Logger logger = Logger.getLogger(ConsoleLoanService.class);

    @Autowired
    private MQWrapperClient mqWrapperClient;

    private void beginPayroll(long payrollId) {
        mqWrapperClient.sendMessage(MessageQueue.Payroll, String.valueOf(payrollId));
    }
}
