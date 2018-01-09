package com.tuotiansudai.activity.service;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.activity.repository.dto.NewmanTyrantPrizeDto;
import com.tuotiansudai.activity.repository.mapper.InvestCelebrationHeroRankingMapper;
import com.tuotiansudai.activity.repository.model.NewmanTyrantView;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.model.InvestProductTypeView;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.JsonConverter;
import com.tuotiansudai.util.MobileEncryptor;
import com.tuotiansudai.util.RedisWrapperClient;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class YearEndAwardsActivityService {

    static Logger logger = Logger.getLogger(YearEndAwardsActivityService.class);

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    @Value(value = "${activity.year.end.awards.startTime}")
    private String activityYearEndAwardsStartTime;

    @Value(value = "${activity.year.end.awards.rankTime}")
    private String activityYearEndAwardsRankTime;

    @Value(value = "${activity.year.end.awards.endTime}")
    private String activityYearEndAwardsEndTime;

    private static final String NEWMAN_TYRANT_PRIZE_KEY = "console:Newman_Tyrant_Prize";

    @Autowired
    private InvestCelebrationHeroRankingMapper investCelebrationHeroRankingMapper;

    @Autowired
    private InvestMapper investMapper;

    private final List<AnnualizedAmount> annualizedAmounts = Lists.newArrayList(
            new AnnualizedAmount(200000000l, 600000000l, 0.002),
            new AnnualizedAmount(600000000l, 800000000l, 0.004),
            new AnnualizedAmount(800000000l, 1500000000l, 0.006),
            new AnnualizedAmount(1500000000l, 2000000000l, 0.008),
            new AnnualizedAmount(2000000000l, Long.MAX_VALUE, 0.01));

    public List<NewmanTyrantView> obtainRank(Date tradingTime) {
        if (tradingTime == null) {
            logger.info("tradingTime is null");
            return null;
        }
        tradingTime = new DateTime(tradingTime).withTimeAtStartOfDay().plusDays(1).minusMillis(1).toDate();
        return investCelebrationHeroRankingMapper.findCelebrationHeroRankingByTradingTime(tradingTime, activityYearEndAwardsStartTime, activityYearEndAwardsRankTime);
    }

    public String encryptMobileForWeb(String loginName, String encryptLoginName, String encryptMobile) {
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

    public Map<String, String> annualizedAmountAndRewards(String loginName){

        List<InvestProductTypeView> list = investMapper.findAmountOrderByNameAndProductType(
                DateTime.parse(activityYearEndAwardsStartTime, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate(),
                DateTime.parse(activityYearEndAwardsEndTime, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate(), "岁末专享", null);
        Map<String, Long> amountMaps = list.stream().collect(Collectors.toMap(k -> k.getLoginName(), v -> v.getSumAmount() * v.getProductType().getDuration() / 360, (v, newV) -> v + newV));
        long sumAnnualizedAmount = amountMaps.values().stream().mapToLong(Long::longValue).sum();

        Optional<AnnualizedAmount> reward = annualizedAmounts.stream().filter(annualizedAmount -> annualizedAmount.getMinAmount() <= sumAnnualizedAmount && sumAnnualizedAmount < annualizedAmount.getMaxAmount()).findAny();
        long userRewards = amountMaps.containsKey(loginName) ? new Double(amountMaps.get(loginName) * (reward.map(o->o.getRatio()).orElse(0D))).longValue() : 0;
        double ratio = sumAnnualizedAmount * 1.0 / 2000000000l ;
        if (reward.isPresent()){
            AnnualizedAmount annualizedAmount = reward.get();
            long amount1 = annualizedAmount.getMaxAmount() - annualizedAmount.getMinAmount();
            long amount2 = sumAnnualizedAmount - annualizedAmount.getMinAmount();
            ratio = annualizedAmount.getRatio() * 100  + amount2 * 0.2 / amount1;
        }

        DecimalFormat format = new DecimalFormat("0.00");
        format.setRoundingMode(RoundingMode.FLOOR);
        return Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("sumAnnualizedAmount", format.format(sumAnnualizedAmount / 1000000D))
                .put("rewards", AmountConverter.convertCentToString(userRewards))
                .put("ratio", String.valueOf(ratio < 1 ? (int)Math.floor(ratio * 100) : 100))
                .build());
    }

    class AnnualizedAmount{
        private long minAmount;
        private long maxAmount;
        private double ratio;

        public AnnualizedAmount() {
        }

        public AnnualizedAmount(long minAmount, long maxAmount, double ratio) {
            this.minAmount = minAmount;
            this.maxAmount = maxAmount;
            this.ratio = ratio;
        }

        public long getMinAmount() {
            return minAmount;
        }

        public long getMaxAmount() {
            return maxAmount;
        }

        public double getRatio() {
            return ratio;
        }
    }
}
