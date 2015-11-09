package com.tuotiansudai.job;

import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.repository.model.LoanStatus;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class DeadlineFundraisingJob implements Job{

    @Autowired
    private LoanMapper loanMapper;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        long loanId = (Long)context.getJobDetail().getJobDataMap().get("loanId");
        LoanModel loanModel = loanMapper.findById(loanId);
        if (loanModel.getStatus() == LoanStatus.RAISING && !loanModel.getFundraisingEndTime().after(new Date())) {
            loanMapper.updateStatus(loanModel.getId(), LoanStatus.RECHECK);
        }
    }

}
