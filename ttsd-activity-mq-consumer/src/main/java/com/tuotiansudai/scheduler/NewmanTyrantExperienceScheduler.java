package com.tuotiansudai.scheduler;

import com.google.common.collect.Lists;
import com.tuotiansudai.activity.repository.model.NewmanTyrantHistoryView;
import com.tuotiansudai.activity.repository.model.NewmanTyrantView;
import com.tuotiansudai.activity.service.NewmanTyrantService;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.message.NewmanTyrantMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.util.DateConvertUtil;
import org.apache.commons.lang.time.DateFormatUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class NewmanTyrantExperienceScheduler {
    static Logger logger = LoggerFactory.getLogger(NewmanTyrantExperienceScheduler.class);
    @Value("#{'${activity.newmanTyrant.activity.period}'.split('\\~')}")
    private List<String> newmanTyrantActivityPeriod = Lists.newArrayList();
    @Autowired
    private NewmanTyrantService newmanTyrantService;
    @Autowired
    private RedisWrapperClient redisWrapperClient;
    @Autowired
    private MQWrapperClient mqWrapperClient;

    private static final String NEWMAN_TYRANT_GRANTED_LIST = "NEWMAN_TYRANT_GRANTED_LIST";



    @Scheduled(cron = "0 39 18 * * ?", zone = "Asia/Shanghai")
    public void grantNewmanTyrantExperience() {

        Date grantDate = new DateTime(new Date()).minusDays(1).withTimeAtStartOfDay().toDate();
        Date activityBeginTime = DateConvertUtil.withTimeAtStartOfDay(newmanTyrantActivityPeriod.get(0), "yyyy-MM-dd");
        Date activityEndTime = DateConvertUtil.withTimeAtStartOfDay(newmanTyrantActivityPeriod.get(1), "yyyy-MM-dd");
        if (grantDate.compareTo(activityBeginTime)== -1 || grantDate.compareTo(activityEndTime) == 1){
            logger.info(String.format("[NewmanTyrantExperienceScheduler %s] activity is over", DateFormatUtils.format(grantDate, "yyyy-MM-dd")));
            return;
        }
        logger.info(String.format("[NewmanTyrantExperienceScheduler %s] start...", DateFormatUtils.format(grantDate, "yyyy-MM-dd")));
        List<NewmanTyrantHistoryView> newmanTyrantRanking = newmanTyrantService.obtainNewmanTyrantHistoryRanking(grantDate);
        newmanTyrantRanking.stream()
                .filter(i->!redisWrapperClient.hexists(NEWMAN_TYRANT_GRANTED_LIST,DateFormatUtils.format(i.getCurrentDate(), "yyyy-MM-dd")))
                .forEach(newmanTyrantHistoryView -> this.grantExperience(newmanTyrantHistoryView,grantDate));

        logger.info(String.format("[NewmanTyrantExperienceScheduler %s] end...", DateFormatUtils.format(grantDate, "yyyy-MM-dd")));
    }

    private void grantExperience(NewmanTyrantHistoryView newmanTyrantHistoryView,Date grantDate) {
        NewmanTyrantHistoryView.type type = newmanTyrantHistoryView.obtainNewmanTyrantType();
        logger.info(String.format("[NewmanTyrantExperienceScheduler %s] grant type %s ...",DateFormatUtils.format(grantDate, "yyyy-MM-dd"),type.name()));
        List<NewmanTyrantView> grantList = Lists.newArrayList();

        if(type == NewmanTyrantHistoryView.type.NEWMAN || type == NewmanTyrantHistoryView.type.NEWMAN_TYRANT){
            grantList.addAll(newmanTyrantService.obtainNewman(newmanTyrantHistoryView.getCurrentDate()));
        }

        if(type == NewmanTyrantHistoryView.type.TYRANT || type == NewmanTyrantHistoryView.type.NEWMAN_TYRANT){
            grantList.addAll(newmanTyrantService.obtainTyrant(newmanTyrantHistoryView.getCurrentDate()));
        }

        grantList.stream().forEach(newmanTyrantView -> this.grantExperience(newmanTyrantHistoryView,
                newmanTyrantView));

    }

    private void grantExperience(NewmanTyrantHistoryView newmanTyrantHistoryView,
                                 NewmanTyrantView newmanTyrantView){
        logger.info(String.format("[NewmanTyrantExperienceScheduler %s] grant %s experience  begin ...",
                DateFormatUtils.format(newmanTyrantHistoryView.getCurrentDate(), "yyyy-MM-dd"),
                newmanTyrantView.getLoginName()));

        mqWrapperClient.sendMessage(MessageQueue.InvestNewmanTyrant_AssignExperience,new NewmanTyrantMessage(newmanTyrantHistoryView.getCurrentDate(),
                newmanTyrantView.getLoginName()));

        logger.info(String.format("[NewmanTyrantExperienceScheduler %s] grant %s experience  success ...",
                DateFormatUtils.format(newmanTyrantHistoryView.getCurrentDate(), "yyyy-MM-dd"),
                newmanTyrantView.getLoginName()));

    }


}
