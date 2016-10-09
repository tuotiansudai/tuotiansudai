package com.tuotiansudai.service.impl;

import com.google.common.collect.Lists;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.dto.MysteriousPrizeDto;
import com.tuotiansudai.membership.repository.mapper.MembershipMapper;
import com.tuotiansudai.membership.repository.mapper.UserMembershipMapper;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.model.ActivityCategory;
import com.tuotiansudai.repository.model.HeroRankingView;
import com.tuotiansudai.service.HeroRankingService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class HeroRankingServiceImpl implements HeroRankingService {

    static Logger logger = Logger.getLogger(HeroRankingServiceImpl.class);

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    private final static String MYSTERIOUSREDISKEY = "console:mysteriousPrize";

    @Value("#{'${web.heroRanking.activity.period}'.split('\\~')}")
    private List<String> heroRankingActivityPeriod = Lists.newArrayList();

    @Value("#{'${activity.new.heroRanking.period}'.split('\\~')}")
    private List<String> newHeroRankingActivityPeriod = Lists.newArrayList();

    @Autowired
    private MembershipMapper membershipMapper;

    @Autowired
    private UserMembershipMapper userMembershipMapper;

    @Autowired
    private AccountMapper accountMapper;

    private int lifeSecond = 5184000;

    @Override
    public List<HeroRankingView> obtainHeroRanking(ActivityCategory activityCategory,Date tradingTime) {
        if (tradingTime == null) {
            logger.debug("tradingTime is null");
            return null;
        }
        tradingTime = new DateTime(tradingTime).withTimeAtStartOfDay().plusDays(1).minusMillis(1).toDate();

        List<String> activityPeriod = getActivityPeriod(activityCategory);
        List<HeroRankingView> heroRankingViews = investMapper.findHeroRankingByTradingTime(tradingTime, activityPeriod.get(0), activityPeriod.get(1));

        return CollectionUtils.isNotEmpty(heroRankingViews) && heroRankingViews.size() > 10 ? heroRankingViews.subList(0, 10) : heroRankingViews;
    }

    @Override
    public List<HeroRankingView> obtainHeroRankingReferrer(ActivityCategory activityCategory,Date tradingTime) {
        List<String> activityPeriod = getActivityPeriod(activityCategory);
        return investMapper.findHeroRankingByReferrer(tradingTime, activityPeriod.get(0), activityPeriod.get(1), 0, 10);
    }

    @Override
    public void saveMysteriousPrize(MysteriousPrizeDto mysteriousPrizeDto) {
        String prizeDate = new DateTime(mysteriousPrizeDto.getPrizeDate()).withTimeAtStartOfDay().toString("yyyy-MM-dd");
        redisWrapperClient.hsetSeri(MYSTERIOUSREDISKEY, prizeDate, mysteriousPrizeDto,lifeSecond);
    }

    @Override
    public MysteriousPrizeDto obtainMysteriousPrizeDto(String prizeDate) {
        return (MysteriousPrizeDto) redisWrapperClient.hgetSeri(MYSTERIOUSREDISKEY, prizeDate);
    }

    private List getActivityPeriod(ActivityCategory activityCategory){
        return activityCategory.equals(ActivityCategory.HERO_RANKING) ? heroRankingActivityPeriod : newHeroRankingActivityPeriod;
    }
}
