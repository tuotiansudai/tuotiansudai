package com.tuotiansudai.activity.job;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.tuotiansudai.activity.repository.mapper.LuxuryPrizeMapper;
import com.tuotiansudai.activity.repository.mapper.TravelPrizeMapper;
import com.tuotiansudai.activity.repository.mapper.UserLuxuryPrizeMapper;
import com.tuotiansudai.activity.repository.mapper.UserTravelPrizeMapper;
import com.tuotiansudai.activity.repository.model.LuxuryPrizeModel;
import com.tuotiansudai.activity.repository.model.TravelPrizeModel;
import com.tuotiansudai.activity.repository.model.UserLuxuryPrizeModel;
import com.tuotiansudai.activity.repository.model.UserTravelPrizeModel;
import com.tuotiansudai.client.RedisWrapperClient;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class CalculateTravelLuxuryPrizeJob implements Job {
    static Logger logger = Logger.getLogger(CalculateTravelLuxuryPrizeJob.class);

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Value(value = "activity.autumn.travel.invest")
    private String activityAutumnTravelInvestKey;

    @Value(value = "activity.autumn.luxury.invest")
    private String activityAutumnLuxuryInvestKey;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.autumn.startTime}\")}")
    private Date activityAutumnStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.autumn.endTime}\")}")
    private Date activityAutumnEndTime;

    @Autowired
    private UserTravelPrizeMapper userTravelPrizeMapper;

    @Autowired
    private UserLuxuryPrizeMapper userLuxuryPrizeMapper;

    @Autowired
    private LuxuryPrizeMapper luxuryPrizeMapper;

    @Autowired
    private TravelPrizeMapper travelPrizeMapper;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Date currentDate = new Date();
        if (currentDate.before(this.activityAutumnStartTime) || currentDate.after(this.activityAutumnEndTime)) {
            return;
        }
        logger.debug("[Calculate travel  prize begin...]");
//        calculateTravelPrize();
        logger.debug("[Calculate travel  prize end...]");


        logger.debug("[Calculate travel  luxury begin...]");
        calculateLuxuryPrize();
        logger.debug("[Calculate travel  luxury end...]");


    }

    private void calculateTravelPrize() {
        DateTime yesterday = new DateTime().minusDays(1).withTimeAtStartOfDay();
        Set travelKeySet = redisWrapperClient.hkeys(activityAutumnTravelInvestKey);
        List<TravelPrizeModel> travelPrizeModels = travelPrizeMapper.findAll();

        for (Object travelKey : travelKeySet) {
            String[] travelInvest = String.valueOf(travelKey).split(":");
            if (travelInvest.length < 4) {
                logger.error(MessageFormat.format("[calculate travel:] key {0} is invalid", travelKey));
                continue;
            }
            String loginName = travelInvest[0];
            String mobile = travelInvest[1];
            String userName = travelInvest[2];
            if (!yesterday.isEqual(new DateTime(travelInvest[3]).withTimeAtStartOfDay())) {
                continue;
            }
            String travelValue = redisWrapperClient.hget(activityAutumnTravelInvestKey, String.valueOf(travelKey));
            if (Strings.isNullOrEmpty(travelValue)) {
                logger.error(MessageFormat.format("[calculate travel:] key-{0} value is null ", travelKey));
                continue;
            }

            long sumInvestAmount = Long.parseLong(travelValue.split("\\|")[0]);

            for (TravelPrizeModel travelPrizeModel : travelPrizeModels) {
                if (sumInvestAmount >= travelPrizeModel.getInvestAmount()) {
                    List<UserTravelPrizeModel> userTravels = userTravelPrizeMapper.findMobileAndCreatedTime(mobile, yesterday.toDate());
                    if(CollectionUtils.isEmpty(userTravels)){
                        UserTravelPrizeModel userTravelPrizeModel = new UserTravelPrizeModel(travelPrizeModel.getId(), travelPrizeModel.getName(), loginName, mobile, userName, sumInvestAmount);
                        userTravelPrizeModel.setCreatedTime(yesterday.toDate());
                        userTravelPrizeMapper.create(userTravelPrizeModel);
                    }

                    break;
                }
            }

        }
    }

    private void calculateLuxuryPrize() {
        DateTime yesterday = new DateTime().minusDays(1).withTimeAtStartOfDay();
        Set luxuryKeySet = redisWrapperClient.hkeys(activityAutumnLuxuryInvestKey);
        List<LuxuryPrizeModel> luxuryPrizeModels = luxuryPrizeMapper.findAll();

        for (Object luxuryKey : luxuryKeySet) {
            String[] luxuryInvest = String.valueOf(luxuryKey).split(":");
            if (luxuryInvest.length < 4) {
                logger.error(MessageFormat.format("[calculate luxury:] key {0} is invalid", String.valueOf(luxuryKey)));
                continue;
            }
            String loginName = luxuryInvest[0];
            String mobile = luxuryInvest[1];
            String userName = luxuryInvest[2];
            if (!yesterday.isEqual(new DateTime(luxuryInvest[3]).withTimeAtStartOfDay())) {
                continue;
            }
            String luxuryValue = redisWrapperClient.hget(activityAutumnLuxuryInvestKey, String.valueOf(luxuryKey));
            if (Strings.isNullOrEmpty(luxuryValue)) {
                logger.error(MessageFormat.format("[calculate luxury:] key-{0} value is null ", luxuryValue));
                continue;
            }

            long sumInvestAmount = Long.parseLong(luxuryValue.split("\\|")[0]);

            for (LuxuryPrizeModel luxuryPrizeModel : luxuryPrizeModels) {
                List<UserLuxuryPrizeModel> userLuxury = userLuxuryPrizeMapper.findMobileAndCreatedTime(mobile, yesterday.toDate());
                if(CollectionUtils.isEmpty(userLuxury)){
                    String prizeName = getLuxuryPrizeName(luxuryPrizeModel, sumInvestAmount);
                    if(StringUtils.isNotEmpty(prizeName)){

                        UserLuxuryPrizeModel userLuxuryPrizeModel = new UserLuxuryPrizeModel(luxuryPrizeModel.getId(), prizeName, loginName, mobile, userName, sumInvestAmount);
                        userLuxuryPrizeModel.setCreatedTime(yesterday.toDate());
                        userLuxuryPrizeMapper.create(userLuxuryPrizeModel);
                        break;
                    }

                }

            }

        }
    }

    private String getLuxuryPrizeName(LuxuryPrizeModel luxuryPrizeModel, long investAmount) {
        String prizeName = "{0}{1}{2}";

        Map<Long, String> luxuryPrizeLinkedHashMap = Maps.newLinkedHashMap(new ImmutableMap.Builder<Long, String>()
                .put(luxuryPrizeModel.getInvestAmount(), luxuryPrizeModel.getBrand() + luxuryPrizeModel.getName())
                .put(luxuryPrizeModel.getThirtyPercentOffInvestAmount(), MessageFormat.format(prizeName, luxuryPrizeModel.getBrand(), luxuryPrizeModel.getName(), "7折券"))
                .put(luxuryPrizeModel.getTwentyPercentOffInvestAmount(), MessageFormat.format(prizeName, luxuryPrizeModel.getBrand(), luxuryPrizeModel.getName(), "8折券"))
                .put(luxuryPrizeModel.getTenPercentOffInvestAmount(), MessageFormat.format(prizeName, luxuryPrizeModel.getBrand(), luxuryPrizeModel.getName(), "8折券"))
                .build());


        for (Map.Entry<Long, String> luxuryEntry : luxuryPrizeLinkedHashMap.entrySet()) {
            if (investAmount >= luxuryEntry.getKey() ) {
                return luxuryEntry.getValue();
            }
        }

        return null;

    }
}
