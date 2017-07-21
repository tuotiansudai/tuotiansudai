package com.tuotiansudai.scheduler.activity;

import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.message.ExperienceAssigningMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.activity.service.HouseDecorateService;
import org.apache.commons.lang.time.DateFormatUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class HouseDecorateExperienceScheduler {
    static Logger logger = LoggerFactory.getLogger(HouseDecorateExperienceScheduler.class);

    @Autowired
    private HouseDecorateService houseDecorateService;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Value(value = "${activity.house.decorate.startTime}")
    private String startTime;

    @Value(value = "${activity.house.decorate.endTime}}")
    private String endTime;

    @Scheduled(cron = "0 0 0 ＊ * ?", zone = "Asia/Shanghai")
    public void grantFamilyFinanceExperience() {

        Date grantDate = new DateTime(new Date()).withTimeAtStartOfDay().minusMillis(1).toDate();
        Date grantExperienceStartTime = DateTime.parse(startTime, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).plusDays(1).toDate();
        Date grantExperienceEndTime = DateTime.parse(endTime, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).plusDays(1).toDate();
        if(grantDate.compareTo(grantExperienceStartTime)== -1 || grantDate.compareTo(grantExperienceEndTime) == 1){
            logger.info(String.format("[FamilyFinanceExperienceScheduler %s] activity grand experience is over", DateFormatUtils.format(grantDate, "yyyy-MM-dd")));
            return;
        }

        logger.info(String.format("[FamilyFinanceExperienceScheduler %s] start...", DateFormatUtils.format(grantDate, "yyyy-MM-dd")));
        List<ExperienceAssigningMessage> experienceAssigningMessages = houseDecorateService.yesterdayObtainExperience();
        if(experienceAssigningMessages == null){
            return;
        }
        for (ExperienceAssigningMessage experienceAssigningMessage : experienceAssigningMessages) {
            grandExperience(experienceAssigningMessage,grantDate);
        }

        logger.info(String.format("[FamilyFinanceExperienceScheduler %s] end...", DateFormatUtils.format(grantDate, "yyyy-MM-dd")));
    }


    public void grandExperience(ExperienceAssigningMessage experienceAssigningMessage,Date grantDate){
        logger.info(String.format("[FamilyFinanceExperienceScheduler %s] grant %s experience  begin ...",
                DateFormatUtils.format(grantDate, "yyyy-MM-dd"),
                experienceAssigningMessage.getLoginName()));

        mqWrapperClient.sendMessage(MessageQueue.ExperienceAssigning,experienceAssigningMessage);

        logger.info(String.format("[FamilyFinanceExperienceScheduler %s] grant %s experience  success ...",
                DateFormatUtils.format(grantDate, "yyyy-MM-dd"),
                experienceAssigningMessage.getLoginName()));
    }

}

