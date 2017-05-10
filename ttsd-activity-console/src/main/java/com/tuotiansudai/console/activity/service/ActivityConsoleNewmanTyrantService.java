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

    private int lifeSecond = 5184000;

    private static final String NEWMAN_TYRANT_PRIZE_KEY = "console:Newman_Tyrant_Prize";

    public List<NewmanTyrantView> obtainTyrant(Date tradingTime) {
        if (tradingTime == null) {
            logger.info("tradingTime is null");
            return null;
        }
        tradingTime = new DateTime(tradingTime).withTimeAtStartOfDay().plusDays(1).minusMillis(1).toDate();

        List<NewmanTyrantView> tyrantViews = investNewmanTyrantMapper.findNewmanTyrantByTradingTime(tradingTime, newmanTyrantActivityPeriod.get(0), newmanTyrantActivityPeriod.get(1), false);

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

}
