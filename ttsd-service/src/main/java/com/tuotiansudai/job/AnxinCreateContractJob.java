package com.tuotiansudai.job;

import com.tuotiansudai.anxin.service.AnxinSignService;
import com.tuotiansudai.anxin.service.impl.AnxinSignServiceImpl;
import com.tuotiansudai.cfca.dto.AnxinContractType;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.sms.GenerateContractErrorNotifyDto;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.service.InvestService;
import com.tuotiansudai.transfer.repository.mapper.TransferApplicationMapper;
import com.tuotiansudai.transfer.repository.model.TransferApplicationModel;
import com.tuotiansudai.util.JobManager;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

@Component
public class AnxinCreateContractJob implements Job {
    static Logger logger = Logger.getLogger(AnxinCreateContractJob.class);

    public final static String BUSINESS_ID = "BUSINESS_ID";

    public final static int HANDLE_DELAY_MINUTES = 3;

    @Autowired
    private AnxinSignService anxinSignService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        try {
            long businessId = (long) context.getJobDetail().getJobDataMap().get(BUSINESS_ID);
            logger.info(MessageFormat.format("trigger anxin create contract handle job, prepare do job. businessId:{0}", String.valueOf(businessId)));
            anxinSignService.createLoanContracts(businessId);
            logger.info(MessageFormat.format("trigger anxin create contract handle job, execute job end. businessId:{0}", String.valueOf(businessId)));
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            return;
        }
    }
}
