package com.tuotiansudai.activity.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.activity.repository.dto.NewmanTyrantPrizeDto;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.HeroRankingView;
import com.tuotiansudai.repository.model.NewmanTyrantHistoryView;
import com.tuotiansudai.util.DateConvertUtil;
import com.tuotiansudai.util.JsonConverter;
import com.tuotiansudai.util.MobileEncryptor;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Service
public class NewmanTyrantService {
    static Logger logger = Logger.getLogger(NewmanTyrantService.class);
    @Autowired
    private InvestMapper investMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisWrapperClient redisWrapperClient;


    private static final String NEWMAN_TYRANT_PRIZE_KEY = "console:Newman_Tyrant_Prize";

    @Value("#{'${activity.newmanTyrant.activity.period}'.split('\\~')}")
    private List<String> newmanTyrantActivityPeriod = Lists.newArrayList();


    public List<HeroRankingView> obtainNewman(Date tradingTime) {
        if (tradingTime == null) {
            logger.info("tradingTime is null");
            return null;
        }
        tradingTime = new DateTime(tradingTime).withTimeAtStartOfDay().plusDays(1).minusMillis(1).toDate();

        List<HeroRankingView> newmanViews = investMapper.findNewmanTyrantByTradingTime(tradingTime, newmanTyrantActivityPeriod.get(0), newmanTyrantActivityPeriod.get(1), true);

        return CollectionUtils.isNotEmpty(newmanViews) && newmanViews.size() > 3 ? newmanViews.subList(0, 3) : newmanViews;
    }

    public List<HeroRankingView> obtainTyrant(Date tradingTime) {
        if (tradingTime == null) {
            logger.info("tradingTime is null");
            return null;
        }
        tradingTime = new DateTime(tradingTime).withTimeAtStartOfDay().plusDays(1).minusMillis(1).toDate();

        List<HeroRankingView> tyrantViews = investMapper.findNewmanTyrantByTradingTime(tradingTime, newmanTyrantActivityPeriod.get(0), newmanTyrantActivityPeriod.get(1), false);

        return CollectionUtils.isNotEmpty(tyrantViews) && tyrantViews.size() > 10 ? tyrantViews.subList(0, 10) : tyrantViews;
    }

    public String encryptMobileForWeb(String loginName, String encryptLoginName) {
        if (encryptLoginName.equalsIgnoreCase(loginName)) {
            return "您的位置";
        }
        return MobileEncryptor.encryptMiddleMobile(userMapper.findByLoginName(encryptLoginName).getMobile());
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
        List<Date> dates = Lists.newArrayList();
        Date activityBeginTime = DateConvertUtil.withTimeAtStartOfDay(newmanTyrantActivityPeriod.get(0), "yyyy-MM-dd");
        Date activityEndTime = DateConvertUtil.withTimeAtStartOfDay(newmanTyrantActivityPeriod.get(1),"yyyy-MM-dd");
        while (tradingTime.compareTo(activityBeginTime) > -1 && tradingTime.compareTo(activityEndTime) == -1) {
            dates.add(tradingTime);
            tradingTime = new DateTime(tradingTime).minusDays(1).withTimeAtStartOfDay().toDate();
        }
        return dates;
    }

    public List<NewmanTyrantHistoryView> obtainNewmanTyrantHistoryRanking(Date tradingTime) {
        List<NewmanTyrantHistoryView> newmanTyrantHistory = Lists.newArrayList();
        List<Date> dateList = this.obtainActivityDays(tradingTime);
        for (Date currentDate : dateList) {
            List<HeroRankingView> newmanViews = obtainNewman(currentDate);
            List<HeroRankingView> tyrantViews = obtainTyrant(currentDate);
            long avgNewmanInvestAmount = newmanViews.stream().mapToLong(heroRankingView -> heroRankingView.getSumAmount()).sum();
            long avgTyrantInvestAmount = tyrantViews.stream().mapToLong(heroRankingView -> heroRankingView.getSumAmount()).sum();
            avgNewmanInvestAmount = new BigDecimal(avgNewmanInvestAmount).divide(new BigDecimal(newmanViews.size() == 0 ? 1 : newmanViews.size()), 0, RoundingMode.DOWN).longValue();
            avgTyrantInvestAmount = new BigDecimal(avgTyrantInvestAmount).divide(new BigDecimal(tyrantViews.size() == 0 ? 1 : tyrantViews.size()), 0, RoundingMode.DOWN).longValue();
            if (avgNewmanInvestAmount == 0 && avgTyrantInvestAmount == 0) {
                logger.info(String.format("%s : 富豪榜和新贵榜均值均为零!", DateConvertUtil.format(currentDate, "yyyy-MM-dd")));
                continue;
            }
            newmanTyrantHistory.add(new NewmanTyrantHistoryView(DateConvertUtil.format(currentDate, "yyyy-MM-dd"),
                    avgNewmanInvestAmount,
                    avgTyrantInvestAmount));
        }
        return newmanTyrantHistory;
    }
}
