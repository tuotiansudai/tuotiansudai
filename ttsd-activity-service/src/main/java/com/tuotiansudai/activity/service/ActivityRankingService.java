package com.tuotiansudai.activity.service;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.activity.repository.dto.NewmanTyrantPrizeDto;
import com.tuotiansudai.activity.repository.mapper.InvestCelebrationHeroRankingMapper;
import com.tuotiansudai.activity.repository.model.ActivityInvestRanking;
import com.tuotiansudai.activity.repository.model.MyHeroRanking;
import com.tuotiansudai.activity.repository.model.NewmanTyrantView;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.util.JsonConverter;
import com.tuotiansudai.util.MobileEncryptor;
import com.tuotiansudai.util.RedisWrapperClient;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ActivityRankingService {

    static Logger logger = Logger.getLogger(ActivityRankingService.class);

    @Autowired
    private InvestCelebrationHeroRankingMapper investCelebrationHeroRankingMapper;

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    private static final String NEWMAN_TYRANT_PRIZE_KEY = "console:Newman_Tyrant_Prize";

    public List<NewmanTyrantView> obtainRank(Date tradingTime, ActivityInvestRanking activityInvestRanking) {
        if (tradingTime == null) {
            return null;
        }
        tradingTime = new DateTime(tradingTime).withTimeAtStartOfDay().plusDays(1).minusMillis(1).toDate();
        List<String> activityTime = getActivityTime(activityInvestRanking);
        return investCelebrationHeroRankingMapper.findCelebrationHeroRankingByTradingTime(tradingTime, activityTime.get(0), activityTime.get(1));
    }

    public String encryptMobileForWeb(String loginName, String encryptLoginName, String encryptMobile) {
        if (encryptLoginName.equalsIgnoreCase(loginName)) {
            return "您的位置";
        }
        return MobileEncryptor.encryptMiddleMobile(encryptMobile);
    }

    public Map<String, Object> activityHome(String loginName, ActivityInvestRanking activityInvestRanking) {
        List<NewmanTyrantView> rankingViews = obtainRank(new Date(), activityInvestRanking);
        int investRanking = CollectionUtils.isNotEmpty(rankingViews) ?
                Iterators.indexOf(rankingViews.iterator(), input -> input.getLoginName().equalsIgnoreCase(loginName)) + 1 : 0;
        List<String> activityTime = getActivityTime(activityInvestRanking);
        return Maps.newHashMap(ImmutableMap.<String, Object>builder()
                .put("prizeDto", obtainPrizeDto(new DateTime().toString("yyyy-MM-dd")))
                .put("investRanking", investRanking > 10 ? 0 : investRanking)
                .put("investAmount", investRanking > 0 ? rankingViews.get(investRanking - 1).getSumAmount() : 0)
                .put("activityStartTime", activityTime.get(0))
                .put("activityEndTime", activityTime.get(0))
                .build());
    }

    public BasePaginationDataDto<NewmanTyrantView> obtainRanking(Date tradingTime, String loginName, ActivityInvestRanking activityInvestRanking) {
        BasePaginationDataDto<NewmanTyrantView> baseListDataDto = new BasePaginationDataDto<>();
        List<NewmanTyrantView> rankViews = obtainRank(tradingTime, activityInvestRanking);
        rankViews = CollectionUtils.isNotEmpty(rankViews) && rankViews.size() > 10 ? rankViews.subList(0, 10) : rankViews;
        rankViews.forEach(newmanTyrantView -> newmanTyrantView.setLoginName(encryptMobileForWeb(loginName, newmanTyrantView.getLoginName(), newmanTyrantView.getMobile())));
        baseListDataDto.setRecords(rankViews);
        baseListDataDto.setStatus(true);
        return baseListDataDto;
    }

    public MyHeroRanking obtainMyRanking(Date tradingTime, String loginName, ActivityInvestRanking activityInvestRanking) {
        MyHeroRanking myHeroRanking = new MyHeroRanking();
        List<NewmanTyrantView> yearEndAwardsRankViews = obtainRank(tradingTime, activityInvestRanking);
        int investRanking = CollectionUtils.isNotEmpty(yearEndAwardsRankViews) ?
                Iterators.indexOf(yearEndAwardsRankViews.iterator(), input -> input.getLoginName().equalsIgnoreCase(loginName)) + 1 : 0;
        myHeroRanking.setInvestAmount(investRanking > 0 ? yearEndAwardsRankViews.get(investRanking - 1).getSumAmount() : 0);
        myHeroRanking.setInvestRanking(investRanking > 10 ? 0 : investRanking);
        return myHeroRanking;
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

    public List<String> getActivityTime(ActivityInvestRanking activityInvestRanking){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return Lists.newArrayList(simpleDateFormat.format(activityInvestRanking.getStartTime()), simpleDateFormat.format(activityInvestRanking.getEndTime()));
    }
}
