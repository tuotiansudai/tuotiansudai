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

import java.util.Calendar;
import java.util.Date;

public class DragonBoatPKSendExperienceJob implements Job {

    private static final Logger logger = LoggerFactory.getLogger(DragonBoatPKSendExperienceJob.class);

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.dragon.boat.PK.endTime}\")}")
    private static Date endTime;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("[Dragon Boat] PK activity ended, send experience.");
        mqWrapperClient.sendMessage(MessageQueue.DragonBoatPKEndSendExperience, "");
    }

    // 活动结束后，第二天上午9点发放奖品
    public static Date getJobExecuteTime(){
        Calendar c = Calendar.getInstance();
        c.setTime(endTime);
        c.add(Calendar.HOUR, 9);
        return c.getTime();
    }
}
