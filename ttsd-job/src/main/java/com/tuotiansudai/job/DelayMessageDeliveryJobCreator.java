package com.tuotiansudai.job;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tuotiansudai.message.AnxinContractMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.util.JsonConverter;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.quartz.SchedulerException;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DelayMessageDeliveryJobCreator {
    private static Logger logger = Logger.getLogger(DelayMessageDeliveryJobCreator.class);

    private final static int AUTO_LOAN_OUT_DELAY_SECONDS = 30 * 60;

    private final static int ANXIN_CONTRACT_QUERY_DELAY_SECONDS = 60 * 10;

    private final static int PAYROLL_FAIL_WAIT_SECONDS = 30 * 60;

    public static void createAutoLoanOutDelayJob(JobManager jobManager, long loanId) {
        String messageBody = String.valueOf(loanId);
        create(jobManager, AUTO_LOAN_OUT_DELAY_SECONDS, MessageQueue.LoanOut, messageBody, String.valueOf(loanId), true);
    }

    public static void createAnxinContractQueryDelayJob(JobManager jobManager, long businessId, String anxinContractType) {
        try {
            AnxinContractMessage message = new AnxinContractMessage(businessId, anxinContractType);
            String messageBody = JsonConverter.writeValueAsString(message);
            create(jobManager, ANXIN_CONTRACT_QUERY_DELAY_SECONDS, MessageQueue.QueryAnxinContract, messageBody);
        } catch (JsonProcessingException e) {
            logger.error("create query contract job for loan/transfer[" + businessId + "] fail", e);
        }
    }

    public static void createOrReplaceStartRaisingDelayJob(JobManager jobManager, long loanId, Date fundraisingStartTime) {
        String messageBody = String.valueOf(loanId);
        create(jobManager, fundraisingStartTime, MessageQueue.LoanStartRaising, messageBody, String.valueOf(loanId), true);
    }

    public static void createOrReplaceStopRaisingDelayJob(JobManager jobManager, long loanId, Date fundraisingEndTime) {
        String messageBody = String.valueOf(loanId);
        create(jobManager, fundraisingEndTime, MessageQueue.LoanStopRaising, messageBody, String.valueOf(loanId), true);
    }

    public static void createCancelTransferApplicationDelayJob(JobManager jobManager, long transferApplicationId, Date deadline) {
        String messageBody = String.valueOf(transferApplicationId);
        create(jobManager, deadline, MessageQueue.CancelTransferApplication, messageBody, String.valueOf(transferApplicationId), true);
    }

    public static void createOrReplaceCreditLoanBalanceAlertDelayJob(JobManager jobManager, Date sendingTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        create(jobManager, sendingTime, MessageQueue.CreditLoanBalanceAlert, "", "CreditLoanBalanceAlert_" + sdf.format(sendingTime), true);
    }

    public static void create(JobManager jobManager, int delaySeconds, MessageQueue messageQueue, String messageBody) {
        create(jobManager, delaySeconds, messageQueue, messageBody, String.valueOf(System.currentTimeMillis()), true);
    }

    public static void create(JobManager jobManager, Date fireTime, MessageQueue messageQueue, String messageBody) {
        create(jobManager, fireTime, messageQueue, messageBody, String.valueOf(System.currentTimeMillis()), true);
    }

    public static void create(JobManager jobManager, int delaySeconds, MessageQueue messageQueue, String messageBody,
                              String jobKey, boolean replaceExistingJob) {
        Date fireTime = new DateTime().plusSeconds(delaySeconds).toDate();
        create(jobManager, fireTime, messageQueue, messageBody, jobKey, replaceExistingJob);
    }

    public static void create(JobManager jobManager, Date fireTime, MessageQueue messageQueue, String messageBody,
                              String jobKey, boolean replaceExistingJob) {
        try {
            jobManager.newJob(JobType.DelayMessageDelivery, DelayMessageDeliveryJob.class)
                    .addJobData(DelayMessageDeliveryJob.MESSAGE_QUEUE_KEY_NAME, messageQueue)
                    .addJobData(DelayMessageDeliveryJob.MESSAGE_BODY_KEY_NAME, messageBody)
                    .withIdentity(JobType.DelayMessageDelivery.name(), messageQueue.name() + "-" + jobKey)
                    .replaceExistingJob(replaceExistingJob)
                    .runOnceAt(fireTime)
                    .submit();
            logger.info(String.format("create DelayMessageDeliveryJob success, queue name: [%s], message: [%s]", messageQueue.name(), messageBody));
        } catch (SchedulerException e) {
            logger.error("create DelayMessageDeliveryJob failed", e);
        }
    }
}
