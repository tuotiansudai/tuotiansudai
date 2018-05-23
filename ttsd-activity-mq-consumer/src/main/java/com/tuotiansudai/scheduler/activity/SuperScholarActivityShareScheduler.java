package com.tuotiansudai.scheduler.activity;

import com.tuotiansudai.activity.repository.mapper.SuperScholarRewardMapper;
import com.tuotiansudai.activity.repository.model.SuperScholarRewardModel;
import com.tuotiansudai.util.RedisWrapperClient;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

@Component
public class SuperScholarActivityShareScheduler {

    static Logger logger = LoggerFactory.getLogger(SuperScholarActivityShareScheduler.class);

    @Autowired
    private SuperScholarRewardMapper superScholarRewardMapper;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.super.scholar.startTime}\")}")
    private Date activityStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.super.scholar.endTime}\")}")
    private Date activityEndTime;

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    private final String REFERRER_ACTIVITY_SUPER_SCHOLAR_ACCOUNT = "REFERRER_ACTIVITY_SUPER_SCHOLAR_ACCOUNT:{0}:{1}";

    private final String REFERRER_ACTIVITY_SUPER_SCHOLAR_INVEST = "REFERRER_ACTIVITY_SUPER_SCHOLAR_INVEST:{0}:{1}";

    @Scheduled(cron = "0 10 0 * * ?", zone = "Asia/Shanghai")
//    @Scheduled(cron = "0 0/5 * * * ?", zone = "Asia/Shanghai")
    public void updateSuperScholarShareStatus() {

        if (new Date().before(new DateTime(activityStartTime).plusDays(1).toDate())
                || new Date().after(new DateTime(activityEndTime).plusDays(1).toDate())) {
            logger.info("SUPER_SCHOLAR_ACTIVITY update share status is over");
            return;
        }

        String yesterday = DateTimeFormat.forPattern("yyyy-MM-dd").print(DateTime.now().minusDays(1));
//        String yesterday = DateTimeFormat.forPattern("yyyy-MM-dd").print(DateTime.now());
        List<SuperScholarRewardModel> models = superScholarRewardMapper.findByAnswerTime(DateTime.now().minusDays(1).toDate());
//        List<SuperScholarRewardModel> models = superScholarRewardMapper.findByAnswerTime(DateTime.now().toDate());

        for(SuperScholarRewardModel model : models){
            model.setShareAccount(redisWrapperClient.exists(MessageFormat.format(REFERRER_ACTIVITY_SUPER_SCHOLAR_ACCOUNT, yesterday, model.getLoginName())));
            model.setShareInvest(redisWrapperClient.exists(MessageFormat.format(REFERRER_ACTIVITY_SUPER_SCHOLAR_INVEST, yesterday, model.getLoginName())));
            superScholarRewardMapper.update(model);
        }

    }
}
