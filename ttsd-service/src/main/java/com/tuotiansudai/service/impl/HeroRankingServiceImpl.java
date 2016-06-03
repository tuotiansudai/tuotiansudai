package com.tuotiansudai.service.impl;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.model.HeroRankingView;
import com.tuotiansudai.service.HeroRankingService;
import com.tuotiansudai.transfer.repository.mapper.TransferApplicationMapper;
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

    @Override
    public List<HeroRankingView> obtainHeroRanking(Date tradingTime) {
        if(tradingTime == null){
            logger.debug("tradingTime is null");
            return null;
        }
        tradingTime = new DateTime(tradingTime).withTimeAtStartOfDay().plusDays(1).minusMillis(1).toDate();

        List<HeroRankingView> heroRankingViews = investMapper.findHeroRankingByTradingTime(tradingTime);

        return CollectionUtils.isNotEmpty(heroRankingViews) && heroRankingViews.size() > 10?heroRankingViews.subList(0,10):heroRankingViews;
    }

    @Override
    public Integer obtainHeroRankingByLoginName(Date tradingTime, final String loginName) {
        if(tradingTime == null){
            logger.debug("tradingTime is null");
            return null;
        }
        tradingTime = new DateTime(tradingTime).withTimeAtStartOfDay().plusDays(1).minusMillis(1).toDate();
        long count = transferApplicationMapper.findCountTransferApplicationByApplicationTime(loginName,tradingTime);
        if(count > 0){
            return null;
        }
        List<HeroRankingView> heroRankingViews = investMapper.findHeroRankingByTradingTime(tradingTime);

        if(heroRankingViews != null){
            return Iterators.indexOf(heroRankingViews.iterator(), new Predicate<HeroRankingView>() {
                @Override
                public boolean apply(HeroRankingView input) {
                    return input.getLoginName().equals(loginName);
                }
            }) + 1;
        }
        return null;

    }
}
