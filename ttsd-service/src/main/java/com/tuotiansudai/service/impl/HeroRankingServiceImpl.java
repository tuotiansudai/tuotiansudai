package com.tuotiansudai.service.impl;


import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.dto.BaseListDataDto;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.model.HeroRankingView;
import com.tuotiansudai.service.HeroRankingService;
import com.tuotiansudai.util.RandomUtils;
import org.apache.commons.collections4.CollectionUtils;
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
    private RandomUtils randomUtils;

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
    public BaseListDataDto<HeroRankingView> findHeroRankingByReferrer(Date tradingTime, final String loginName) {
        BaseListDataDto<HeroRankingView> baseListDataDto = new BaseListDataDto<>();
        Date startDate = new DateTime("2016-07-01").withTimeAtStartOfDay().toDate();
        Date endDate = new DateTime("2016-08-01").withTimeAtStartOfDay().toDate();
        if (tradingTime.before(startDate) || tradingTime.after(endDate)) {
            baseListDataDto.setStatus(false);
        } else {
            List<HeroRankingView> heroRankingViewList = investMapper.findHeroRankingByReferrer(tradingTime);
            baseListDataDto.setStatus(true);
            baseListDataDto.setRecords(Lists.transform(heroRankingViewList, new Function<HeroRankingView, HeroRankingView>() {
                @Override
                public HeroRankingView apply(HeroRankingView input) {
                    input.setLoginName(randomUtils.encryptLoginName(loginName, input.getLoginName(), 6));
                    return input;
                }
            }));
        }
        return baseListDataDto;
    }

    @Override
    public List<HeroRankingView> obtainHeroRankingByLoginName(Date tradingTime, String loginName) {

        return null;
    }
    
}
