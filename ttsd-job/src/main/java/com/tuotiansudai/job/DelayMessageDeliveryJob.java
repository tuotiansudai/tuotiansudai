package com.tuotiansudai.job;

import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.mq.client.model.MessageQueue;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

public class DelayMessageDeliveryJob implements Job {
    private static Logger logger = Logger.getLogger(DelayMessageDeliveryJob.class);
    static String MESSAGE_QUEUE_KEY_NAME = "MessageQueue";
    static String MESSAGE_BODY_KEY_NAME = "MessageBody";

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            logger.info("prepare to send the delayed message");
            MessageQueue messageQueue = (MessageQueue) context.getJobDetail().getJobDataMap().get(MESSAGE_QUEUE_KEY_NAME);
            String messageBody = context.getJobDetail().getJobDataMap().getString(MESSAGE_BODY_KEY_NAME);
            logger.info(String.format("delayed message queue is [%s], message body is [%s]", messageQueue.name(), messageBody));
            mqWrapperClient.sendMessage(messageQueue, messageBody);
            logger.info("delayed message send success");
        } catch (Exception e) {
            logger.error("send message failed", e);
        }
    }
}
