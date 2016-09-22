package com.tuotiansudai.activity.service;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.MysteriousPrizeDto;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.model.HeroRankingView;
import com.tuotiansudai.transfer.repository.mapper.TransferApplicationMapper;
import com.tuotiansudai.util.RandomUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class HeroRankingService {

    static Logger logger = Logger.getLogger(HeroRankingService.class);

    @Autowired
    private TransferApplicationMapper transferApplicationMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    private final static String MYSTERIOUSREDISKEY = "console:mysteriousPrize";

    @Value("#{'${web.heroRanking.activity.period}'.split('\\~')}")
    private List<String> heroRankingActivityPeriod = Lists.newArrayList();

    @Value("#{'${activity.new.heroRanking.activity.period}'.split('\\~')}")
    private List<String> newHeroRankingActivityPeriod = Lists.newArrayList();

    @Autowired
    private RandomUtils randomUtils;

    public List<HeroRankingView> obtainHeroRanking(Date tradingTime) {
        if (tradingTime == null) {
            logger.debug("tradingTime is null");
            return null;
        }
        tradingTime = new DateTime(tradingTime).withTimeAtStartOfDay().plusDays(1).minusMillis(1).toDate();

        List<HeroRankingView> heroRankingViews = investMapper.findHeroRankingByTradingTime(tradingTime, heroRankingActivityPeriod.get(0), heroRankingActivityPeriod.get(1));

        return CollectionUtils.isNotEmpty(heroRankingViews) && heroRankingViews.size() > 10 ? heroRankingViews.subList(0, 10) : heroRankingViews;
    }

    public Integer obtainHeroRankingByLoginName(Date tradingTime, final String loginName) {
        if (tradingTime == null) {
            logger.debug("tradingTime is null");
            return null;
        }
        tradingTime = new DateTime(tradingTime).withTimeAtStartOfDay().plusDays(1).minusMillis(1).toDate();
        long count = transferApplicationMapper.findCountTransferApplicationByApplicationTime(loginName, tradingTime, heroRankingActivityPeriod.get(0));
        if (count > 0) {
            return null;
        }
        List<HeroRankingView> heroRankingViews = investMapper.findHeroRankingByTradingTime(tradingTime, heroRankingActivityPeriod.get(0), heroRankingActivityPeriod.get(1));
        if (heroRankingViews != null) {
            return Iterators.indexOf(heroRankingViews.iterator(), new Predicate<HeroRankingView>() {
                @Override
                public boolean apply(HeroRankingView input) {
                    return input.getLoginName().equalsIgnoreCase(loginName);
                }
            }) + 1;
        }
        return null;
    }

    public Integer findHeroRankingByReferrerLoginName(final String loginName) {
        List<HeroRankingView> heroRankingViews = investMapper.findHeroRankingByReferrer(new Date(), heroRankingActivityPeriod.get(0), heroRankingActivityPeriod.get(1), 0, 20);
        if (CollectionUtils.isEmpty(heroRankingViews)) {
            return null;
        }
        return Iterators.indexOf(heroRankingViews.iterator(), new Predicate<HeroRankingView>() {
            @Override
            public boolean apply(HeroRankingView input) {
                return input.getLoginName().equalsIgnoreCase(loginName);
            }
        }) + 1;
    }

    public MysteriousPrizeDto obtainMysteriousPrizeDto(String prizeDate) {
        return (MysteriousPrizeDto) redisWrapperClient.hgetSeri(MYSTERIOUSREDISKEY, prizeDate);
    }

    public BasePaginationDataDto<HeroRankingView> findHeroRankingByReferrer(Date tradingTime, final String loginName, int index, int pageSize) {
        BasePaginationDataDto<HeroRankingView> baseListDataDto = new BasePaginationDataDto<>();

        Date activityBeginTime = DateTime.parse(heroRankingActivityPeriod.get(0), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();

        Date activityEndTime = DateTime.parse(heroRankingActivityPeriod.get(1), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();

        if (tradingTime.before(activityBeginTime) || tradingTime.after(activityEndTime)) {
            baseListDataDto.setStatus(false);
        } else {
            List<HeroRankingView> heroRankingViewList = investMapper.findHeroRankingByReferrer(tradingTime, heroRankingActivityPeriod.get(0), heroRankingActivityPeriod.get(1), (index - 1) * pageSize, pageSize);
            baseListDataDto.setStatus(true);
            if (CollectionUtils.isNotEmpty(heroRankingViewList)) {
                baseListDataDto.setRecords(Lists.transform(heroRankingViewList, new Function<HeroRankingView, HeroRankingView>() {
                    @Override
                    public HeroRankingView apply(HeroRankingView input) {
                        input.setLoginName(randomUtils.encryptMobile(loginName, input.getLoginName()));
                        return input;
                    }
                }));
            }
        }
        return baseListDataDto;
    }
}
