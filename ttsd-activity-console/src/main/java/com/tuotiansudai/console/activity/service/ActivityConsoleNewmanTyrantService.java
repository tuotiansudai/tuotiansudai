package com.tuotiansudai.console.activity.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.activity.repository.dto.MysteriousPrizeDto;
import com.tuotiansudai.activity.repository.dto.NewmanTyrantPrizeDto;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.model.HeroRankingView;
import com.tuotiansudai.util.JsonConverter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class ActivityConsoleNewmanTyrantService {

    static Logger logger = Logger.getLogger(ActivityConsoleNewmanTyrantService.class);
    @Autowired
    private RedisWrapperClient redisWrapperClient;
    @Autowired
    private InvestMapper investMapper;

    @Value("#{'${activity.newmanTyrant.activity.period}'.split('\\~')}")
    private List<String> newmanTyrantActivityPeriod = Lists.newArrayList();

    private int lifeSecond = 5184000;

    private static final String NEWMAN_TYRANT_PRIZE_KEY = "console:Newman_Tyrant_Prize";

    public List<HeroRankingView> obtainTyrant(Date tradingTime) {
        if (tradingTime == null) {
            logger.info("tradingTime is null");
            return null;
        }
        tradingTime = new DateTime(tradingTime).withTimeAtStartOfDay().plusDays(1).minusMillis(1).toDate();

        List<HeroRankingView> tyrantViews = investMapper.findNewmanTyrantByTradingTime(tradingTime, newmanTyrantActivityPeriod.get(0), newmanTyrantActivityPeriod.get(1), false);

        return CollectionUtils.isNotEmpty(tyrantViews) && tyrantViews.size() > 10 ? tyrantViews.subList(0, 10) : tyrantViews;
    }

    public List<HeroRankingView> obtainNewman(Date tradingTime) {
        if (tradingTime == null) {
            logger.info("tradingTime is null");
            return null;
        }
        tradingTime = new DateTime(tradingTime).withTimeAtStartOfDay().plusDays(1).minusMillis(1).toDate();

        List<HeroRankingView> newmanViews = investMapper.findNewmanTyrantByTradingTime(tradingTime, newmanTyrantActivityPeriod.get(0), newmanTyrantActivityPeriod.get(1), true);

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
