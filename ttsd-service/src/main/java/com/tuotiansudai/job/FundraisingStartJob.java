package com.tuotiansudai.job;

import com.google.common.base.Strings;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.message.service.MessageService;
import com.tuotiansudai.service.LoanCreateService;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class FundraisingStartJob implements Job{
    static Logger logger = Logger.getLogger(FundraisingStartJob.class);

    public final static String LOAN_ID_KEY = "LOAN_ID";

    @Autowired
    private LoanCreateService loanCreateService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("trigger FundraisingStartJob, prepare do job");
        long loanId = Long.parseLong(String.valueOf(context.getJobDetail().getJobDataMap().get(LOAN_ID_KEY)));
        logger.info(MessageFormat.format("trigger FundraisingStartJob, loanId = {0}", String.valueOf(loanId)));

        loanCreateService.startRaising(loanId);
        //TODO: add message
        String messageId = redisWrapperClient.hget(LoanCreateService.LOAN_MESSAGE_REDIS_KEY, String.valueOf(loanId));
        if (!Strings.isNullOrEmpty(messageId)) {
            messageService.approveManualMessage(Long.valueOf(messageId), "SYSTEM");
            redisWrapperClient.hdel(LoanCreateService.LOAN_MESSAGE_REDIS_KEY, String.valueOf(loanId));
        }
    }
}
