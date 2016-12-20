package com.tuotiansudai.job;

import com.google.common.base.Strings;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.enums.PushSource;
import com.tuotiansudai.enums.PushType;
import com.tuotiansudai.message.PushMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.repository.mapper.LoanDetailsMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.LoanDetailsModel;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.repository.model.LoanStatus;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class FundraisingStartJob implements Job {
    static Logger logger = Logger.getLogger(FundraisingStartJob.class);

    public final static String LOAN_ID_KEY = "LOAN_ID";

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private LoanDetailsMapper loanDetailsMapper;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("trigger FundraisingStartJob, prepare do job");
        long loanId = Long.parseLong(String.valueOf(context.getJobDetail().getJobDataMap().get(LOAN_ID_KEY)));
        logger.info(MessageFormat.format("trigger FundraisingStartJob, loanId = {0}", String.valueOf(loanId)));

        LoanModel loanModel = loanMapper.findById(loanId);
        LoanDetailsModel loanDetailsModel = loanDetailsMapper.getByLoanId(loanId);
        if (loanModel != null && LoanStatus.PREHEAT == loanModel.getStatus()) {
            loanMapper.updateStatus(loanId, LoanStatus.RAISING);
            if (!Strings.isNullOrEmpty(loanDetailsModel.getPushMessage())) {
                mqWrapperClient.sendMessage(MessageQueue.PushMessage, new PushMessage(null, PushSource.ALL, PushType.PREHEAT, loanDetailsModel.getPushMessage()));
            }
        }
    }
}
