package com.tuotiansudai.job;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tuotiansudai.message.AnxinContractQueryMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.util.JsonConverter;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.quartz.SchedulerException;

import java.util.Date;
import java.util.List;

public class DelayMessageDeliveryJobCreator {
    private static Logger logger = Logger.getLogger(CouponRepayNotifyCallbackJob.class);

    private final static int AnxinContractQueryDelaySeconds = 3 * 60;

    public static void createAnxinContractQueryDelayJob(JobManager jobManager, long businessId, String anxinContractType, List<String> batchNo) {
        try {
            AnxinContractQueryMessage message = new AnxinContractQueryMessage(businessId, batchNo, anxinContractType);
            String messageBody = JsonConverter.writeValueAsString(message);
            create(jobManager, AnxinContractQueryDelaySeconds, MessageQueue.QueryAnxinContract, messageBody);
        } catch (JsonProcessingException e) {
            logger.error("create query contract job for loan/transfer[" + businessId + "] fail", e);
        }
    }

    public static void create(JobManager jobManager, int delaySeconds, MessageQueue messageQueue, String messageBody) {
        Date fireTime = new DateTime().plusSeconds(delaySeconds).toDate();
        create(jobManager, fireTime, messageQueue, messageBody);
    }

    public static void create(JobManager jobManager, Date fireTime, MessageQueue messageQueue, String messageBody) {
        try {
            jobManager.newJob(JobType.Default, DelayMessageDeliveryJob.class)
                    .addJobData(DelayMessageDeliveryJob.MESSAGE_QUEUE_KEY_NAME, messageQueue.name())
                    .addJobData(DelayMessageDeliveryJob.MESSAGE_BODY_KEY_NAME, messageBody)
                    .withIdentity(JobType.Default.name(), messageQueue.name() + "-" + System.currentTimeMillis())
                    .runOnceAt(fireTime)
                    .submit();
        } catch (SchedulerException e) {
            logger.error("create DelayMessageDeliveryJob failed", e);
        }
    }
}
