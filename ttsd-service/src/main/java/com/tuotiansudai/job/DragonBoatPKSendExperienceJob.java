package com.tuotiansudai.job;

import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.mq.client.model.MessageQueue;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

public class DragonBoatPKSendExperienceJob implements Job {

    private static final Logger logger = LoggerFactory.getLogger(DragonBoatPKSendExperienceJob.class);

    public static String endTime = "2017-05-24 17:00:00";

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("[Dragon Boat] PK activity ended, send experience.");
        mqWrapperClient.sendMessage(MessageQueue.DragonBoatPKEndSendExperience, "");
    }

}
