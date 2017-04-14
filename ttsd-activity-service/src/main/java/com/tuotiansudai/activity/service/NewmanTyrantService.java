package com.tuotiansudai.activity.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.activity.repository.dto.NewmanTyrantPrizeDto;
import com.tuotiansudai.activity.repository.mapper.InvestNewmanTyrantMapper;
import com.tuotiansudai.activity.repository.model.NewmanTyrantHistoryView;
import com.tuotiansudai.activity.repository.model.NewmanTyrantView;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.util.DateConvertUtil;
import com.tuotiansudai.util.JsonConverter;
import com.tuotiansudai.util.MobileEncryptor;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

@Service
public class NewmanTyrantService {
    static Logger logger = Logger.getLogger(NewmanTyrantService.class);
    @Autowired
    private InvestNewmanTyrantMapper investNewmanTyrantMapper;
    @Autowired
    private RedisWrapperClient redisWrapperClient;


    private static final String NEWMAN_TYRANT_PRIZE_KEY = "console:Newman_Tyrant_Prize";

    @Value("#{'${activity.newmanTyrant.activity.period}'.split('\\~')}")
    private List<String> newmanTyrantActivityPeriod = Lists.newArrayList();


    public List<NewmanTyrantView> obtainNewman(Date tradingTime) {
        if (tradingTime == null) {
            logger.info("tradingTime is null");
            return null;
        }
        tradingTime = new DateTime(tradingTime).withTimeAtStartOfDay().plusDays(1).minusMillis(1).toDate();

        List<NewmanTyrantView> newmanViews = investNewmanTyrantMapper.findNewmanTyrantByTradingTime(tradingTime, newmanTyrantActivityPeriod.get(0), newmanTyrantActivityPeriod.get(1), true);

        return CollectionUtils.isNotEmpty(newmanViews) && newmanViews.size() > 3 ? newmanViews.subList(0, 3) : newmanViews;
    }

    public List<NewmanTyrantView> obtainTyrant(Date tradingTime) {
        if (tradingTime == null) {
            logger.info("tradingTime is null");
            return null;
        }
        tradingTime = new DateTime(tradingTime).withTimeAtStartOfDay().plusDays(1).minusMillis(1).toDate();

        List<NewmanTyrantView> tyrantViews = investNewmanTyrantMapper.findNewmanTyrantByTradingTime(tradingTime, newmanTyrantActivityPeriod.get(0), newmanTyrantActivityPeriod.get(1), false);

        return CollectionUtils.isNotEmpty(tyrantViews) && tyrantViews.size() > 10 ? tyrantViews.subList(0, 10) : tyrantViews;
    }

    public String encryptMobileForWeb(String loginName,String encryptLoginName, String encryptMobile) {
        if (encryptLoginName.equalsIgnoreCase(loginName)) {
            return "您的位置";
        }
        return MobileEncryptor.encryptMiddleMobile(encryptMobile);
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

    private List<Date> obtainActivityDays(Date tradingTime) {
        tradingTime = new DateTime(tradingTime).withTimeAtStartOfDay().toDate();
        List<Date> dates = Lists.newArrayList();
        Date activityBeginTime = DateTime.parse(newmanTyrantActivityPeriod.get(0), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        Date activityEndTime = DateTime.parse(newmanTyrantActivityPeriod.get(1), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
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
            if (avgNewmanInvestAmount == 0 && avgTyrantInvestAmount == 0) {
                logger.info(String.format("%s : 富豪榜和新贵榜均值均为零!", DateConvertUtil.format(currentDate, "yyyy-MM-dd")));
                continue;
            }
            newmanTyrantHistory.add(new NewmanTyrantHistoryView(currentDate,
                    avgNewmanInvestAmount,
                    avgTyrantInvestAmount));
        }
        return newmanTyrantHistory;
    }

    public List<String> getActivityTime(){

        return Lists.newArrayList(newmanTyrantActivityPeriod.get(0),newmanTyrantActivityPeriod.get(1));
    }
}
