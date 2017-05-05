package com.tuotiansudai.activity.service;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.activity.repository.dto.MysteriousPrizeDto;
import com.tuotiansudai.activity.repository.model.ActivityCategory;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.HeroRankingView;
import com.tuotiansudai.repository.mapper.TransferApplicationMapper;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.MobileEncryptor;
import com.tuotiansudai.util.RedisWrapperClient;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class HeroRankingService {

    static Logger logger = Logger.getLogger(HeroRankingService.class);

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private TransferApplicationMapper transferApplicationMapper;

    private final static String MYSTERIOUSREDISKEY = "console:mysteriousPrize";

    @Value("#{'${web.heroRanking.activity.period}'.split('\\~')}")
    private List<String> heroRankingActivityPeriod = Lists.newArrayList();

    @Value("#{'${activity.new.heroRanking.period}'.split('\\~')}")
    private List<String> newHeroRankingActivityPeriod = Lists.newArrayList();

    @Value(value = "${activity.lanternFestival.startTime}")
    private String lanternFestivalStartTime;
    @Value(value = "${activity.lanternFestival.endTime}")
    private String lanternFestivalEndTime;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

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

    public Map obtainHeroRankingAndInvestAmountByLoginName(ActivityCategory activityCategory,Date tradingTime, final String loginName) {
        if (tradingTime == null) {
            logger.info("tradingTime is null");
            return null;
        }
        List<String> activityPeriod = getActivityPeriod(activityCategory);
        tradingTime  = new DateTime(tradingTime).withTimeAtStartOfDay().plusDays(1).minusMillis(1).toDate();
        long count = transferApplicationMapper.findCountTransferApplicationByApplicationTime(loginName, tradingTime, activityPeriod.get(0));
        if (count > 0) {
            return Maps.newHashMap(ImmutableMap.<String, String>builder()
                    .put("investRanking", "0")
                    .put("activityStartTime", activityPeriod.get(0))
                    .put("activityEndTime", activityPeriod.get(1))
                    .put("investAmount", "0").build());
        }

        int investRanking = 0;
        String investAmount = "0";
        List<HeroRankingView> heroRankingViews = investMapper.findHeroRankingByTradingTime(tradingTime, activityPeriod.get(0), activityPeriod.get(1));
        if (heroRankingViews != null) {
            investRanking = Iterators.indexOf(heroRankingViews.iterator(),
                    input -> input.getLoginName().equalsIgnoreCase(loginName)) + 1;

            if (investRanking > 0) {
                investAmount = AmountConverter.convertCentToString(heroRankingViews.get(investRanking - 1).getSumAmount());
            }
        }
        return Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("investRanking", String.valueOf(investRanking))
                .put("activityStartTime", activityPeriod.get(0))
                .put("activityEndTime", activityPeriod.get(1))
                .put("investAmount", investAmount).build());
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
                baseListDataDto.setRecords(Lists.transform(heroRankingViewList, input -> {
                    if (input.getLoginName().equalsIgnoreCase(loginName)) {
                        input.setLoginName("您的位置");
                        return input;
                    }
                    input.setLoginName(MobileEncryptor.encryptMiddleMobile(userMapper.findByLoginName(input.getLoginName()).getMobile()));
                    return input;
                }));
            }
        }
        return baseListDataDto;
    }


    public Integer findHeroRankingByReferrerLoginName(ActivityCategory activityCategory,final String loginName) {
        List<String> activityPeriod = getActivityPeriod(activityCategory);
        List<HeroRankingView> heroRankingViews = investMapper.findHeroRankingByReferrer(new Date(), activityPeriod.get(0), activityPeriod.get(1), 0, 20);
        if (CollectionUtils.isEmpty(heroRankingViews)) {
            return null;
        }
        return Iterators.indexOf(heroRankingViews.iterator(), input -> input.getLoginName().equalsIgnoreCase(loginName)) + 1;
    }

    public List<String> getActivityPeriod(ActivityCategory activityCategory){
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

    public List<String> getActivityTime(){
        Date startTime = DateTime.parse(newHeroRankingActivityPeriod.get(0), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        Date endTime = DateTime.parse(newHeroRankingActivityPeriod.get(1), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();

        return Lists.newArrayList(sdf.format(startTime),sdf.format(endTime));
    }

    public MysteriousPrizeDto obtainMysteriousPrizeDto(String prizeDate) {
        return (MysteriousPrizeDto) redisWrapperClient.hgetSeri(MYSTERIOUSREDISKEY, prizeDate);
    }
}
