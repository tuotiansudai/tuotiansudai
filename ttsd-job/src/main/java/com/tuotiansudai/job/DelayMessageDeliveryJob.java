package com.tuotiansudai.job;

import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.mq.client.model.MessageQueue;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

public class DelayMessageDeliveryJob implements Job {
    private static Logger logger = Logger.getLogger(CouponRepayNotifyCallbackJob.class);
    static String MESSAGE_QUEUE_KEY_NAME = "MessageQueue";
    static String MESSAGE_BODY_KEY_NAME = "MessageBody";

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            MessageQueue messageQueue = (MessageQueue) context.getJobDetail().getJobDataMap().get(MESSAGE_QUEUE_KEY_NAME);
            String messageBody = context.getJobDetail().getJobDataMap().getString(MESSAGE_BODY_KEY_NAME);
            mqWrapperClient.sendMessage(messageQueue, messageBody);
        } catch (Exception e) {
            logger.error("send message failed", e);
        }
    }
}
