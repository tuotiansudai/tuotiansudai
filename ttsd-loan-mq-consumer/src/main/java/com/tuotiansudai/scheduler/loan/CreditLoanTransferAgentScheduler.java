package com.tuotiansudai.scheduler.loan;

import com.tuotiansudai.client.PayWrapperClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CreditLoanTransferAgentScheduler {
    static Logger logger = LoggerFactory.getLogger(CreditLoanTransferAgentScheduler.class);

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Scheduled(cron = "0 0 0 * * ?", zone = "Asia/Shanghai")
    private void creditLoanTransferAgent() {
        logger.info("[信用贷账户给代理人转账] is start");
        payWrapperClient.creditLoanTransferAgent();
    }
}
