package com.tuotiansudai.job;

import com.tuotiansudai.cfca.dto.AnxinContractType;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class AnxinContractHandleJob implements Job {
    static Logger logger = Logger.getLogger(AnxinContractHandleJob.class);

    public final static String BUSINESS_ID = "BUSINESS_ID";

    public final static String ANXIN_CONTRACT_TYPE = "LOAN_ID";

    public final static int HANDLE_DELAY_MINUTES = 10;

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("trigger anxin contract handle job, prepare do job");

        long businessId;
        AnxinContractType anxinContractType;
        BaseDto<PayDataDto> dto;
        try {
            businessId = (long) context.getJobDetail().getJobDataMap().get(BUSINESS_ID);
            anxinContractType = (AnxinContractType) context.getJobDetail().getJobDataMap().get(ANXIN_CONTRACT_TYPE);
            if(anxinContractType.equals(AnxinContractType.LOAN_CONTRACT)){
                dto = payWrapperClient.anxinContractHandle(businessId);
            }else{
                dto = payWrapperClient.anxinTransferContractHandle(businessId);
            }
            logger.info(MessageFormat.format("trigger anxin contract handle job, loanId : {0},anxin contract type :{1}", String.valueOf(businessId),anxinContractType.name()));
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            return;
        }

        if (!dto.getData().getStatus()) {
            throw new JobExecutionException();
        }
    }
}
