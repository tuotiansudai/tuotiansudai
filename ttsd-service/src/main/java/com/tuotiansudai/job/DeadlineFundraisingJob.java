package com.tuotiansudai.job;

import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.repository.model.LoanStatus;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DeadlineFundraisingJob implements Job{

    static Logger logger = Logger.getLogger(DeadlineFundraisingJob.class);

    @Autowired
    private LoanMapper loanMapper;

    public final static String LOAN_ID_KEY = "LOAN_ID";

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("trigger DeadlineFundraisingJob");

        Object value;
        try {
            value = context.getJobDetail().getJobDataMap().get(LOAN_ID_KEY);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            return;
        }

        if (value == null) {
            return;
        }

        long loanId = Long.parseLong(String.valueOf(value)) ;

        LoanModel loanModel = loanMapper.findById(loanId);

        logger.info("loanId = " + loanId);
        logger.info("status = " + loanModel.getStatus());
        logger.info("fundraisingEndTime = " + loanModel.getFundraisingEndTime());

        if (loanModel.getStatus() == LoanStatus.RAISING) {
            loanMapper.updateStatus(loanModel.getId(), LoanStatus.RECHECK);
        }
    }

}
