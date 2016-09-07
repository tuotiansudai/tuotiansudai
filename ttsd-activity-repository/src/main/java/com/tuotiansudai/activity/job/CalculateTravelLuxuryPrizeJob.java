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
import java.util.*;

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
        Date yesterday = new DateTime().minusDays(1).withTimeAtStartOfDay().toDate();

        if (yesterday.before(this.activityAutumnStartTime) || yesterday.after(this.activityAutumnEndTime)) {
            return;
        }
        logger.debug("[calculate travel  prize begin...]");
        calculateTravelPrize();
        logger.debug("[calculate travel  prize end...]");


        logger.debug("[calculate travel  luxury begin...]");
        calculateLuxuryPrize();
        logger.debug("[calculate travel  luxury end...]");


    }

    private void calculateTravelPrize() {
        DateTime yesterday = new DateTime().minusDays(1).withTimeAtStartOfDay();
        Set travelKeySet = redisWrapperClient.hkeys(activityAutumnTravelInvestKey);
        List<TravelPrizeModel> travelPrizeModels = travelPrizeMapper.findAll();
        logger.debug("[calculate travel] date:" + yesterday.toString());
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
                    if (CollectionUtils.isEmpty(userTravels)) {
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
        logger.debug("[calculate luxury] date:" + yesterday.toString());

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

            List<UserLuxuryPrizeModel> userLuxury = userLuxuryPrizeMapper.findMobileAndCreatedTime(mobile, yesterday.toDate());
            if (CollectionUtils.isEmpty(userLuxury)) {
                String prizeName = getLuxuryPrizeName(luxuryPrizeModels, sumInvestAmount);
                if (StringUtils.isNotEmpty(prizeName)) {
                    long luxuryPrizeId = Long.parseLong(prizeName.split("\\|")[0]);
                    UserLuxuryPrizeModel userLuxuryPrizeModel = new UserLuxuryPrizeModel(luxuryPrizeId, prizeName.split("\\|")[1], loginName, mobile, userName, sumInvestAmount);
                    userLuxuryPrizeModel.setCreatedTime(yesterday.toDate());
                    userLuxuryPrizeMapper.create(userLuxuryPrizeModel);
                }

            }


        }
    }

    private String getLuxuryPrizeName(List<LuxuryPrizeModel> luxuryPrizeModels, long investAmount) {
        Map<Long, String> luxuryPrizeTreeMap = Maps.newTreeMap(new Comparator<Long>() {
            @Override
            public int compare(Long o1, Long o2) {
                return o2.compareTo(o1);
            }
        });

        for (LuxuryPrizeModel luxuryPrizeModel : luxuryPrizeModels) {
            luxuryPrizeTreeMap.put(luxuryPrizeModel.getInvestAmount(), MessageFormat.format("{0}|{1}", luxuryPrizeModel.getId(), luxuryPrizeModel.getName()));
            luxuryPrizeTreeMap.put(luxuryPrizeModel.getThirtyPercentOffInvestAmount(), MessageFormat.format("{0}|{1}{2}", luxuryPrizeModel.getId(), luxuryPrizeModel.getName(), "7折券"));
            luxuryPrizeTreeMap.put(luxuryPrizeModel.getTwentyPercentOffInvestAmount(), MessageFormat.format("{0}|{1}{2}", luxuryPrizeModel.getId(), luxuryPrizeModel.getName(), "8折券"));
            luxuryPrizeTreeMap.put(luxuryPrizeModel.getTenPercentOffInvestAmount(), MessageFormat.format("{0}|{1}{2}", luxuryPrizeModel.getId(), luxuryPrizeModel.getName(), "9折券"));
        }

        for (Map.Entry<Long, String> luxuryEntry : luxuryPrizeTreeMap.entrySet()) {
            if (investAmount >= luxuryEntry.getKey()) {
                return luxuryEntry.getValue();
            }
        }

        return null;

    }
}
