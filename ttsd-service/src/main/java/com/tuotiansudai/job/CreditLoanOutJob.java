package com.tuotiansudai.job;

import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.mq.client.model.MessageQueue;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class CreditLoanOutJob implements Job {

    private static final Logger logger = LoggerFactory.getLogger(CreditLoanOutJob.class);

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("[信用贷标的放款] credit loan out start.");
        payWrapperClient.creditLoanOut();
    }

}
