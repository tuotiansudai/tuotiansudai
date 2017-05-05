package com.tuotiansudai.console.activity.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.activity.repository.dto.MysteriousPrizeDto;
import com.tuotiansudai.activity.repository.model.ActivityCategory;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.model.HeroRankingView;
import com.tuotiansudai.util.RedisWrapperClient;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ActivityConsoleHeroRankingService {

    static Logger logger = Logger.getLogger(ActivityConsoleHeroRankingService.class);

    @Autowired
    private InvestMapper investMapper;

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    private final static String MYSTERIOUSREDISKEY = "console:mysteriousPrize";

    @Value("#{'${web.heroRanking.activity.period}'.split('\\~')}")
    private List<String> heroRankingActivityPeriod = Lists.newArrayList();

    @Value("#{'${activity.new.heroRanking.period}'.split('\\~')}")
    private List<String> newHeroRankingActivityPeriod = Lists.newArrayList();

    @Value(value = "${activity.lanternFestival.startTime}")
    private String lanternFestivalStartTime;
    @Value(value = "${activity.lanternFestival.endTime}")
    private String lanternFestivalEndTime;

    private int lifeSecond = 5184000;

    public List<HeroRankingView> obtainHeroRankingReferrer(ActivityCategory activityCategory,Date tradingTime) {
        List<String> activityPeriod = getActivityPeriod(activityCategory);
        return investMapper.findHeroRankingByReferrer(tradingTime, activityPeriod.get(0), activityPeriod.get(1), 0, 10);
    }

    private List getActivityPeriod(ActivityCategory activityCategory){

        switch (activityCategory){
            case HERO_RANKING:
                return heroRankingActivityPeriod;
            case NEW_HERO_RANKING:
                return newHeroRankingActivityPeriod;
            case LANTERN_FESTIVAL_ACTIVITY:
                return Lists.newArrayList(lanternFestivalStartTime,lanternFestivalEndTime);
        }
        return null;
    }

    public List<HeroRankingView> obtainHeroRanking(ActivityCategory activityCategory,Date tradingTime) {
        if (tradingTime == null) {
            logger.info("tradingTime is null");
            return null;
        }
        tradingTime = new DateTime(tradingTime).withTimeAtStartOfDay().plusDays(1).minusMillis(1).toDate();

        List<String> activityPeriod = getActivityPeriod(activityCategory);
        List<HeroRankingView> heroRankingViews = investMapper.findHeroRankingByTradingTime(tradingTime, activityPeriod.get(0), activityPeriod.get(1));

        return CollectionUtils.isNotEmpty(heroRankingViews) && heroRankingViews.size() > 10 ? heroRankingViews.subList(0, 10) : heroRankingViews;
    }

    public MysteriousPrizeDto obtainMysteriousPrizeDto(String prizeDate) {
        logger.info("prizeDate: " + prizeDate);
        return (MysteriousPrizeDto) redisWrapperClient.hgetSeri(MYSTERIOUSREDISKEY, prizeDate);
    }

    public void saveMysteriousPrize(MysteriousPrizeDto mysteriousPrizeDto) {
        String prizeDate = new DateTime(mysteriousPrizeDto.getPrizeDate()).withTimeAtStartOfDay().toString("yyyy-MM-dd");
        redisWrapperClient.hsetSeri(MYSTERIOUSREDISKEY, prizeDate, mysteriousPrizeDto,lifeSecond);
    }

}
