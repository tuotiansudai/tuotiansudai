package com.tuotiansudai.job;

import com.tuotiansudai.transfer.service.InvestTransferService;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransferApplyAutoCancelJob implements Job{

    static Logger logger = Logger.getLogger(TransferApplyAutoCancelJob.class);

    @Autowired
    private InvestTransferService investTransferService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        long id = (long) context.getJobDetail().getJobDataMap().get("Transfer-apply-id");
        logger.debug("TransferApplyAutoCancelJob===========in, id = " + id);
        investTransferService.investTransferApplyCancel(id);
        logger.debug("TransferApplyAutoCancelJob===========out, id = " + id );
    }

}
