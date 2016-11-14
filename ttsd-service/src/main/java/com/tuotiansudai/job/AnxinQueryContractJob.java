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
public class AnxinQueryContractJob implements Job {
    static Logger logger = Logger.getLogger(AnxinQueryContractJob.class);

    public final static String BUSINESS_ID = "BUSINESS_ID";

    public final static String BATCH_NO_LIST = "BATCH_NO_LIST";

    public final static String ANXIN_CONTRACT_TYPE = "LOAN_ID";

    public final static int HANDLE_DELAY_MINUTES = 10;

    public final static String ANXIN_CONTRACT_QUERY_TRY_TIMES_KEY = "anxinContractQueryTryTimes:";

    @Autowired
    private AnxinSignService anxinSignService;

    @Autowired
    private InvestService investService;

    @Autowired
    private JobManager jobManager;

    @Autowired
    private SmsWrapperClient smsWrapperClient;

    @Value("#{'${anxin.contract.notify.mobileList}'.split('\\|')}")
    private List<String> mobileList;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Autowired
    private TransferApplicationMapper transferApplicationMapper;

    private final static int SEVEN_DAYS = 60 * 60 * 24 * 7; // 7天

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        try {
            logger.info("trigger anxin contract handle job, prepare do job");

            long businessId = (long) context.getJobDetail().getJobDataMap().get(BUSINESS_ID);

            List<String> batchNoList = (List<String>) context.getJobDetail().getJobDataMap().get(BATCH_NO_LIST);

            AnxinContractType anxinContractType = (AnxinContractType) context.getJobDetail().getJobDataMap().get(ANXIN_CONTRACT_TYPE);


            if (redisWrapperClient.incrEx(ANXIN_CONTRACT_QUERY_TRY_TIMES_KEY + businessId, SEVEN_DAYS) > 3) {

                // 尝试超过3次（第4次了），清空计数器，不再尝试了
                redisWrapperClient.del(ANXIN_CONTRACT_QUERY_TRY_TIMES_KEY + businessId);

                // 发短信报警
                smsWrapperClient.sendGenerateContractErrorNotify(new GenerateContractErrorNotifyDto(mobileList, businessId));

                if (anxinContractType == AnxinContractType.LOAN_CONTRACT) {
                    // 清redis中的inCreating标记
                    redisWrapperClient.del(AnxinSignServiceImpl.LOAN_CONTRACT_IN_CREATING_KEY + businessId);
                } else if (anxinContractType == AnxinContractType.TRANSFER_CONTRACT) {
                    // 清redis中的inCreating标记
                    redisWrapperClient.del(AnxinSignServiceImpl.TRANSFER_CONTRACT_IN_CREATING_KEY + businessId);
                }
            }

            List<String> waitingBatchNo = anxinSignService.queryContract(businessId, batchNoList, anxinContractType);
            logger.info(MessageFormat.format("trigger anxin contract handle job, loanId:{0}, anxin contract type:{1}", String.valueOf(businessId), anxinContractType.name()));

            if (waitingBatchNo != null && waitingBatchNo.size() > 0) {
                logger.error(MessageFormat.format("some batch is still in waiting. businessId:{0}, anxin ContractType:{1}, batchNo list(in waiting):{2}",
                        String.valueOf(businessId), anxinContractType.name(), String.join(",", waitingBatchNo)));
                this.createAnxinQueryContractJob(waitingBatchNo, businessId, anxinContractType);
            } else {
                // 查询结束，清空计数器
                redisWrapperClient.del(ANXIN_CONTRACT_QUERY_TRY_TIMES_KEY + businessId);

                logger.debug(MessageFormat.format("execute query contract over. businessId:{0}", String.valueOf(businessId)));

                // 没有待处理的 batchNo 了，检查该 businessId 下的投资是否已经全部成功
                if (anxinContractType == AnxinContractType.LOAN_CONTRACT) {
                    List<InvestModel> contractFailList = investService.findContractFailInvest(businessId);
                    if (CollectionUtils.isNotEmpty(contractFailList)) {
                        logger.error(MessageFormat.format("some batch is fail. send sms. businessId:{0}", String.valueOf(businessId)));
                        // 有失败的，发短信
                        smsWrapperClient.sendGenerateContractErrorNotify(new GenerateContractErrorNotifyDto(mobileList, businessId));
                    }
                    // 清redis中的inCreating标记
                    redisWrapperClient.del(AnxinSignServiceImpl.LOAN_CONTRACT_IN_CREATING_KEY + businessId);
                } else if (anxinContractType == AnxinContractType.TRANSFER_CONTRACT) {
                    TransferApplicationModel applicationModel = transferApplicationMapper.findById(businessId);
                    if (applicationModel != null && StringUtils.isEmpty(applicationModel.getContractNo())) {
                        logger.error(MessageFormat.format("some batch is fail. send sms. businessId:{0}", String.valueOf(businessId)));
                        // 失败了，发短信
                        smsWrapperClient.sendGenerateContractErrorNotify(new GenerateContractErrorNotifyDto(mobileList, businessId));
                    }
                    // 清redis中的inCreating标记
                    redisWrapperClient.del(AnxinSignServiceImpl.TRANSFER_CONTRACT_IN_CREATING_KEY + businessId);
                }
            }
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            return;
        }
    }


    private void createAnxinQueryContractJob(List<String> batchNoList, long businessId, AnxinContractType contractType) {
        try {
            Date triggerTime = new DateTime().plusMinutes(HANDLE_DELAY_MINUTES).toDate();

            jobManager.newJob(JobType.ContractResponse, AnxinQueryContractJob.class)
                    .addJobData(BUSINESS_ID, businessId)
                    .addJobData(BATCH_NO_LIST, batchNoList)
                    .addJobData(ANXIN_CONTRACT_TYPE, contractType)
                    .withIdentity(JobType.ContractResponse.name(), "businessId-" + businessId)
                    .replaceExistingJob(true)
                    .runOnceAt(triggerTime)
                    .submit();
        } catch (SchedulerException e) {
            logger.error("create query contract job for loan/transfer[" + businessId + "] fail", e);
        }
    }

}
