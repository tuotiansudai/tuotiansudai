package com.tuotiansudai.service.impl;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.dto.BaseListDataDto;
import com.tuotiansudai.dto.MysteriousPrizeDto;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.model.HeroRankingView;
import com.tuotiansudai.service.HeroRankingService;
import com.tuotiansudai.transfer.repository.mapper.TransferApplicationMapper;
import com.tuotiansudai.util.RandomUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class HeroRankingServiceImpl implements HeroRankingService {

    static Logger logger = Logger.getLogger(HeroRankingServiceImpl.class);

    @Autowired
    private InvestMapper investMapper;
    @Autowired
    private TransferApplicationMapper transferApplicationMapper;

    @Autowired
    private RandomUtils randomUtils;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    private final static String MYSTERIOUSREDISKEY = "console:mysteriousPrize";

    @Override
    public List<HeroRankingView> obtainHeroRanking(Date tradingTime) {
        if (tradingTime == null) {
            logger.debug("tradingTime is null");
            return null;
        }
        tradingTime = new DateTime(tradingTime).withTimeAtStartOfDay().plusDays(1).minusMillis(1).toDate();

        List<HeroRankingView> heroRankingViews = investMapper.findHeroRankingByTradingTime(tradingTime);

        return CollectionUtils.isNotEmpty(heroRankingViews) && heroRankingViews.size() > 10?heroRankingViews.subList(0,10):heroRankingViews;
    }

    @Override
    public Integer obtainHeroRankingByLoginName(Date tradingTime, final String loginName) {
        if (tradingTime == null) {
            logger.debug("tradingTime is null");
            return null;
        }
        tradingTime = new DateTime(tradingTime).withTimeAtStartOfDay().plusDays(1).minusMillis(1).toDate();
        long count = transferApplicationMapper.findCountTransferApplicationByApplicationTime(loginName, tradingTime);
        if (count > 0) {
            return null;
        }
        List<HeroRankingView> heroRankingViews = investMapper.findHeroRankingByTradingTime(tradingTime);
        if (heroRankingViews != null) {
            return Iterators.indexOf(heroRankingViews.iterator(), new Predicate<HeroRankingView>() {
                @Override
                public boolean apply(HeroRankingView input) {
                    return input.getLoginName().equals(loginName);
                }
            }) + 1;
        }
        return null;
    }

    @Override
    public void saveMysteriousPrize(MysteriousPrizeDto mysteriousPrizeDto) {
        String prizeDate = new DateTime().withTimeAtStartOfDay().toString();
        redisWrapperClient.hsetSeri(MYSTERIOUSREDISKEY,prizeDate,mysteriousPrizeDto);
    }

    @Override
    public BaseListDataDto<HeroRankingView> findHeroRankingByReferrer(Date tradingTime, final String loginName, int index, int pageSize) {
        BaseListDataDto<HeroRankingView> baseListDataDto = new BaseListDataDto<>();
        Date startDate = new DateTime("2016-07-01").withTimeAtStartOfDay().toDate();
        Date endDate = new DateTime("2016-08-01").withTimeAtStartOfDay().toDate();
        if (tradingTime.before(startDate) || tradingTime.after(endDate)) {
            baseListDataDto.setStatus(false);
        } else {
            List<HeroRankingView> heroRankingViewList = investMapper.findHeroRankingByReferrer(tradingTime, (index - 1) * pageSize, pageSize);
            baseListDataDto.setStatus(true);
            if (CollectionUtils.isNotEmpty(heroRankingViewList)) {
                baseListDataDto.setRecords(Lists.transform(heroRankingViewList, new Function<HeroRankingView, HeroRankingView>() {
                    @Override
                    public HeroRankingView apply(HeroRankingView input) {
                        input.setLoginName(randomUtils.encryptLoginName(loginName, input.getLoginName(), 6));
                        return input;
                    }
                }));
            }
        }
        return baseListDataDto;
    }

    @Override
    public Integer findHeroRankingByReferrerLoginName(final String loginName) {
        List<HeroRankingView> heroRankingViews = investMapper.findHeroRankingByReferrer(new Date(), 0, 20);
        if (CollectionUtils.isEmpty(heroRankingViews)) {
            return null;
        }
        return Iterators.indexOf(heroRankingViews.iterator(), new Predicate<HeroRankingView>() {
            @Override
            public boolean apply(HeroRankingView input) {
                return loginName.equalsIgnoreCase(input.getLoginName());
            }
        });
    }

}
