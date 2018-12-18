package com.tuotiansudai.console.activity.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.activity.repository.dto.NewmanTyrantPrizeDto;
import com.tuotiansudai.activity.repository.mapper.InvestNewmanTyrantMapper;
import com.tuotiansudai.activity.repository.model.NewmanTyrantHistoryView;
import com.tuotiansudai.activity.repository.model.NewmanTyrantView;
import com.tuotiansudai.util.JsonConverter;
import com.tuotiansudai.util.RedisWrapperClient;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ActivityConsoleNewmanTyrantService {

    static Logger logger = Logger.getLogger(ActivityConsoleNewmanTyrantService.class);

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    @Autowired
    private InvestNewmanTyrantMapper investNewmanTyrantMapper;

    @Value("#{'${activity.newmanTyrant.activity.period}'.split('\\~')}")
    private List<String> newmanTyrantActivityPeriod = Lists.newArrayList();

    @Value("#{'${activity.celebrationHeroRanking.activity.period}'.split('\\~')}")
    private List<String> celebrationHeroRankingActivityPeriod = Lists.newArrayList();

    @Value(value = "${activity.national.day.startTime}")
    private String activityNationalDayStartTime;

    @Value(value = "${activity.national.day.endTime}")
    private String activityNationalDayEndTime;

    @Value(value = "${activity.year.end.awards.startTime}")
    private String activityYearEndAwardsStartTime;

    @Value(value = "${activity.year.end.awards.rankTime}")
    private String activityYearEndAwardsRankTime;

    @Value(value = "${activity.spring.breeze.startTime}")
    private String activitySpringBreezeStartTime;

    @Value(value = "${activity.spring.breeze.endTime}")
    private String activitySpringBreezeEndTime;

    @Value("#{'${activity.middleautum.nationalday.activity.period}'.split('\\~')}")
    private List<String> middleautumNationalDayActivityPeriod = Lists.newArrayList();

    private int lifeSecond = 5184000;

    private static final String NEWMAN_TYRANT_PRIZE_KEY = "console:Newman_Tyrant_Prize";

    public List<NewmanTyrantView> obtainTyrant(Date tradingTime) {
        if (tradingTime == null) {
            logger.info("tradingTime is null");
            return null;
        }

        List<NewmanTyrantView> tyrantViews = new ArrayList<>();
        List<String> list = this.activityDate(tradingTime);
        if (list.size() > 0) {
            tyrantViews = investNewmanTyrantMapper.findNewmanTyrantByTradingTime(tradingTime, list.get(0), list.get(1), false);
        }
        return CollectionUtils.isNotEmpty(tyrantViews) && tyrantViews.size() > 10 ? tyrantViews.subList(0, 10) : tyrantViews;

    }

    public List<NewmanTyrantView> obtainNewman(Date tradingTime) {
        if (tradingTime == null) {
            logger.info("tradingTime is null");
            return null;
        }
        tradingTime = new DateTime(tradingTime).withTimeAtStartOfDay().plusDays(1).minusMillis(1).toDate();

        List<NewmanTyrantView> newmanViews = investNewmanTyrantMapper.findNewmanTyrantByTradingTime(tradingTime, newmanTyrantActivityPeriod.get(0), newmanTyrantActivityPeriod.get(1), true);

        return CollectionUtils.isNotEmpty(newmanViews) && newmanViews.size() > 3 ? newmanViews.subList(0, 3) : newmanViews;
    }


    public void savePrize(NewmanTyrantPrizeDto newmanTyrantPrizeDto) {
        String prizeDate = new DateTime(newmanTyrantPrizeDto.getPrizeDate()).withTimeAtStartOfDay().toString("yyyy-MM-dd");
        try {
            newmanTyrantPrizeDto = this.completeNewmanTyrantPrizeDto(newmanTyrantPrizeDto, prizeDate);
            String dtoToString = JsonConverter.writeValueAsString(newmanTyrantPrizeDto);
            redisWrapperClient.hset(NEWMAN_TYRANT_PRIZE_KEY, prizeDate, dtoToString, lifeSecond);
        } catch (IOException e) {
            logger.error("savePrize Json format error", e);
        }
    }

    private List<Date> obtainActivityDays(Date tradingTime) {
        List<Date> dates = Lists.newArrayList();
        Date activityBeginTime = new Date();
        Date activityEndTime = new Date();

        if (this.activityDate(tradingTime).size() > 0) {
            activityBeginTime = DateTime.parse(this.activityDate(tradingTime).get(0), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
            activityEndTime = DateTime.parse(this.activityDate(tradingTime).get(1), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        }
        tradingTime = new DateTime(tradingTime).withTimeAtStartOfDay().toDate();
        while (tradingTime.compareTo(activityBeginTime) > -1 && tradingTime.compareTo(activityEndTime) <= 0) {
            dates.add(tradingTime);
            tradingTime = new DateTime(tradingTime).minusDays(1).withTimeAtStartOfDay().toDate();
        }
        return dates;
    }

    public List<NewmanTyrantHistoryView> obtainNewmanTyrantHistoryRanking(Date tradingTime) {
        List<NewmanTyrantHistoryView> newmanTyrantHistory = Lists.newArrayList();
        List<Date> dateList = this.obtainActivityDays(tradingTime);
        for (Date currentDate : dateList) {
            List<NewmanTyrantView> newmanViews = obtainNewman(currentDate);
            List<NewmanTyrantView> tyrantViews = obtainTyrant(currentDate);
            long avgNewmanInvestAmount = newmanViews.stream().mapToLong(heroRankingView -> heroRankingView.getSumAmount()).sum();
            long avgTyrantInvestAmount = tyrantViews.stream().mapToLong(heroRankingView -> heroRankingView.getSumAmount()).sum();
            avgNewmanInvestAmount = new BigDecimal(avgNewmanInvestAmount).divide(new BigDecimal(newmanViews.size() == 0 ? 1 : newmanViews.size()), 0, RoundingMode.DOWN).longValue();
            avgTyrantInvestAmount = new BigDecimal(avgTyrantInvestAmount).divide(new BigDecimal(tyrantViews.size() == 0 ? 1 : tyrantViews.size()), 0, RoundingMode.DOWN).longValue();
            newmanTyrantHistory.add(new NewmanTyrantHistoryView(currentDate,
                    avgNewmanInvestAmount,
                    avgTyrantInvestAmount));
        }
        return newmanTyrantHistory;
    }

    private NewmanTyrantPrizeDto completeNewmanTyrantPrizeDto(NewmanTyrantPrizeDto newmanTyrantPrizeDtoNow, String prizeDate) throws IOException {
        if (redisWrapperClient.hexists(NEWMAN_TYRANT_PRIZE_KEY, prizeDate)) {
            NewmanTyrantPrizeDto newmanTyrantPrizeDto = JsonConverter.readValue(redisWrapperClient.hget(NEWMAN_TYRANT_PRIZE_KEY, prizeDate), NewmanTyrantPrizeDto.class);
            if (StringUtils.isNotEmpty(newmanTyrantPrizeDtoNow.getGoldImageUrl())) {
                newmanTyrantPrizeDto.setGoldImageUrl(newmanTyrantPrizeDtoNow.getGoldImageUrl());
            }
            if (StringUtils.isNotEmpty(newmanTyrantPrizeDtoNow.getGoldPrizeName())) {
                newmanTyrantPrizeDto.setGoldPrizeName(newmanTyrantPrizeDtoNow.getGoldPrizeName());
            }
            if (StringUtils.isNotEmpty(newmanTyrantPrizeDtoNow.getSilverImageUrl())) {
                newmanTyrantPrizeDto.setSilverImageUrl(newmanTyrantPrizeDtoNow.getSilverImageUrl());
            }
            if (StringUtils.isNotEmpty(newmanTyrantPrizeDtoNow.getSilverPrizeName())) {
                newmanTyrantPrizeDto.setSilverPrizeName(newmanTyrantPrizeDtoNow.getSilverPrizeName());
            }

            return newmanTyrantPrizeDto;
        }

        return newmanTyrantPrizeDtoNow;
    }

    public NewmanTyrantPrizeDto obtainPrizeDto(String prizeDate) {
        logger.info("prizeDate: " + prizeDate);
        if (redisWrapperClient.hexists(NEWMAN_TYRANT_PRIZE_KEY, prizeDate)) {
            try {
                NewmanTyrantPrizeDto newmanTyrantPrizeDto = JsonConverter.readValue(redisWrapperClient.hget(NEWMAN_TYRANT_PRIZE_KEY, prizeDate), NewmanTyrantPrizeDto.class);
                return newmanTyrantPrizeDto;
            } catch (IOException e) {
                logger.error("obtainPrizeDto Json format error", e);
            }
        }
        return null;
    }

    public List<String> activityDate(Date tradingTime) {
        List<String> list = new ArrayList<>();
        if (!stringToDate(activityNationalDayStartTime).after(tradingTime) && !tradingTime.after(stringToDate(activityNationalDayEndTime))) {
            list.add(activityNationalDayStartTime);
            list.add(activityNationalDayEndTime);
        }
        if (!stringToDate(newmanTyrantActivityPeriod.get(0)).after(tradingTime) && !tradingTime.after(stringToDate(newmanTyrantActivityPeriod.get(1)))) {
            list.add(newmanTyrantActivityPeriod.get(0));
            list.add(newmanTyrantActivityPeriod.get(1));
        }
        if (!stringToDate(celebrationHeroRankingActivityPeriod.get(0)).after(tradingTime) && !tradingTime.after(stringToDate(celebrationHeroRankingActivityPeriod.get(1)))) {
            list.add(celebrationHeroRankingActivityPeriod.get(0));
            list.add(celebrationHeroRankingActivityPeriod.get(1));
        }
        if (!stringToDate(activityYearEndAwardsStartTime).after(tradingTime) && !tradingTime.after(stringToDate(activityYearEndAwardsRankTime))) {
            list.add(activityYearEndAwardsStartTime);
            list.add(activityYearEndAwardsRankTime);
        }
        if (!stringToDate(activitySpringBreezeStartTime).after(tradingTime) && !tradingTime.after(stringToDate(activitySpringBreezeEndTime))) {
            list.add(activitySpringBreezeStartTime);
            list.add(activitySpringBreezeEndTime);
        }
        return list;
    }

    public Date stringToDate(String date) {
        return DateTime.parse(date, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
    }

    public List<NewmanTyrantView> obtainNewmanViaMiddleAutum(Date tradingTime) {
        if (tradingTime == null) {
            logger.info("【MiddleAutumAndNationalDayService.obtainNewman】参数为空tradingTime");
            return null;
        }
        Date tradingStartTime = null;
        Date tradingEndTime = null;
        //基准比较时间---查询时间当天的22点
        Date standardDate = new DateTime(tradingTime).withTime(22, 0, 0, 0).toDate();
        if (standardDate.before(tradingTime)) {
            tradingStartTime = standardDate;
            tradingEndTime = new DateTime(tradingStartTime).plusDays(1).toDate();
        } else {
            tradingEndTime = standardDate;
            tradingStartTime = new DateTime(tradingEndTime).minusDays(1).toDate();
        }

        List<NewmanTyrantView> newmanViews = investNewmanTyrantMapper.findNewmanTyrantByTradingTimeWithEnd(tradingStartTime, tradingEndTime, middleautumNationalDayActivityPeriod.get(0), middleautumNationalDayActivityPeriod.get(1));
        return CollectionUtils.isNotEmpty(newmanViews) && newmanViews.size() > 10 ? newmanViews.subList(0, 10) : newmanViews;
    }
}
