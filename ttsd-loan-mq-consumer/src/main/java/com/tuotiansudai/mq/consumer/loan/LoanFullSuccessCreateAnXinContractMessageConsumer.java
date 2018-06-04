package com.tuotiansudai.mq.consumer.loan;

import com.google.common.base.Strings;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.tuotiansudai.client.AnxinWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.fudian.message.BankLoanFullMessage;
import com.tuotiansudai.job.DelayMessageDeliveryJobCreator;
import com.tuotiansudai.job.JobManager;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.repository.model.AnxinContractType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class LoanFullSuccessCreateAnXinContractMessageConsumer implements MessageConsumer {
    private static Logger logger = LoggerFactory.getLogger(LoanFullSuccessCreateAnXinContractMessageConsumer.class);

    private final AnxinWrapperClient anxinWrapperClient;

    private final JobManager jobManager;

    @Autowired
    public LoanFullSuccessCreateAnXinContractMessageConsumer(AnxinWrapperClient anxinWrapperClient, JobManager jobManager) {
        this.anxinWrapperClient = anxinWrapperClient;
        this.jobManager = jobManager;
    }

    @Override
    public MessageQueue queue() {
        return MessageQueue.LoanFull_GenerateAnXinContract;
    }

    @Override
    public void consume(String message) {
        logger.info("[Loan Full] receive message: {}: {}.", this.queue(), message);
        if (Strings.isNullOrEmpty(message)) {
            logger.error("[Loan Full] receive message is empty");
            return;
        }

        try {
            BankLoanFullMessage bankLoanFullMessage = new GsonBuilder().create().fromJson(message, BankLoanFullMessage.class);
            BaseDto baseDto = anxinWrapperClient.createLoanContract(bankLoanFullMessage.getLoanId());
            if (!baseDto.isSuccess()) {
                logger.error("[Loan Full] generate anxin contract failure. loanId:{}", bankLoanFullMessage.getLoanId());
                return;
            }

            DelayMessageDeliveryJobCreator.createAnxinContractQueryDelayJob(jobManager, bankLoanFullMessage.getLoanId(), AnxinContractType.LOAN_CONTRACT.name());
        } catch (JsonSyntaxException e) {
            logger.error("[Loan Full] parse message failure, message: {}", message);
            return;
        }
        logger.info("[Loan Full] generate anxin contract success, message: {}", message);
    }
}
