package com.tuotiansudai.job;

import com.tuotiansudai.anxin.service.AnxinSignService;
import com.tuotiansudai.cfca.dto.AnxinContractType;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.List;

@Component
public class AnxinQueryContractJob implements Job {
    static Logger logger = Logger.getLogger(AnxinQueryContractJob.class);

    public final static String BUSINESS_ID = "BUSINESS_ID";

    public final static String BATCH_NO_LIST = "BATCH_NO_LIST";

    public final static String ANXIN_CONTRACT_TYPE = "LOAN_ID";

    public final static int HANDLE_DELAY_MINUTES = 10;

    @Autowired
    private AnxinSignService anxinSignService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("trigger anxin contract handle job, prepare do job");

        try {
            long businessId = (long) context.getJobDetail().getJobDataMap().get(BUSINESS_ID);
            List<String> batchNoList = (List<String>) context.getJobDetail().getJobDataMap().get(BATCH_NO_LIST);
            AnxinContractType anxinContractType = (AnxinContractType) context.getJobDetail().getJobDataMap().get(ANXIN_CONTRACT_TYPE);

            BaseDto<PayDataDto> dto = anxinSignService.queryContract(businessId, batchNoList, anxinContractType);
            logger.info(MessageFormat.format("trigger anxin contract handle job, loanId : {0},anxin contract type :{1}", String.valueOf(businessId), anxinContractType.name()));

            if (!dto.isSuccess()) {
                logger.error(MessageFormat.format("anxin contract query job fail. businessId:{0}, anxinContractType:{1}", businessId, anxinContractType.name()));
            }

        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            return;
        }
    }
}
