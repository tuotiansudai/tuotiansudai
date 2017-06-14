package com.tuotiansudai.activity.service;


import com.google.common.collect.Lists;
import com.tuotiansudai.activity.repository.dto.NewmanTyrantPrizeDto;
import com.tuotiansudai.activity.repository.mapper.InvestCelebrationHeroRankingMapper;
import com.tuotiansudai.activity.repository.model.InvestNewmanTyrantModel;
import com.tuotiansudai.activity.repository.model.NewmanTyrantView;
import com.tuotiansudai.util.JsonConverter;
import com.tuotiansudai.util.MobileEncryptor;
import com.tuotiansudai.util.RedisWrapperClient;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class CelebrationHeroRankingService {

    static Logger logger = Logger.getLogger(CelebrationHeroRankingService.class);

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    @Value("#{'${activity.celebrationHeroRanking.activity.period}'.split('\\~')}")
    private List<String> celebrationHeroRankingActivityPeriod = Lists.newArrayList();

    private static final String NEWMAN_TYRANT_PRIZE_KEY = "console:Newman_Tyrant_Prize";

    @Autowired
    private InvestCelebrationHeroRankingMapper investCelebrationHeroRankingMapper;

    public List<NewmanTyrantView> obtainHero(Date tradingTime){
        if (tradingTime == null) {
            logger.info("tradingTime is null");
            return null;
        }

        tradingTime = new DateTime(tradingTime).withTimeAtStartOfDay().plusDays(1).minusMillis(1).toDate();
        List<NewmanTyrantView> celebrationHeroRankingViews=investCelebrationHeroRankingMapper.findCelebrationHeroRankingByTradingTime(tradingTime, celebrationHeroRankingActivityPeriod.get(0), celebrationHeroRankingActivityPeriod.get(1));
        return celebrationHeroRankingViews;

    }

    public String encryptMobileForWeb(String loginName,String encryptLoginName, String encryptMobile) {
        if (encryptLoginName.equalsIgnoreCase(loginName)) {
            return "您的位置";
        }
        return MobileEncryptor.encryptMiddleMobile(encryptMobile);
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


    public List<String> getActivityTime(){
        return Lists.newArrayList(celebrationHeroRankingActivityPeriod.get(0),celebrationHeroRankingActivityPeriod.get(1));
    }


}
