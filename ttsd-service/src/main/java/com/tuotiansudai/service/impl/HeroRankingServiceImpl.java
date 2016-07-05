package com.tuotiansudai.service.impl;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.dto.BaseListDataDto;
import com.tuotiansudai.dto.MysteriousPrizeDto;
import com.tuotiansudai.membership.repository.mapper.MembershipMapper;
import com.tuotiansudai.membership.repository.mapper.UserMembershipMapper;
import com.tuotiansudai.membership.repository.model.MembershipLevel;
import com.tuotiansudai.membership.repository.model.UserMembershipModel;
import com.tuotiansudai.membership.repository.model.UserMembershipType;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.GivenMembership;
import com.tuotiansudai.repository.model.HeroRankingView;
import com.tuotiansudai.repository.model.InvestStatus;
import com.tuotiansudai.service.HeroRankingService;
import com.tuotiansudai.transfer.repository.mapper.TransferApplicationMapper;
import com.tuotiansudai.util.RandomUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
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
    private TransferApplicationMapper transferApplicationMapper;
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RandomUtils randomUtils;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    private final static String MYSTERIOUSREDISKEY = "console:mysteriousPrize";

    @Value("#{'${web.heroRanking.activity.period}'.split('\\~')}")
    private List<String> heroRankingActivityPeriod;

    @Autowired
    private MembershipMapper membershipMapper;

    @Autowired
    private UserMembershipMapper userMembershipMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Override
    public List<HeroRankingView> obtainHeroRanking(Date tradingTime) {
        if (tradingTime == null) {
            logger.debug("tradingTime is null");
            return null;
        }
        tradingTime = new DateTime(tradingTime).withTimeAtStartOfDay().plusDays(1).minusMillis(1).toDate();

        List<HeroRankingView> heroRankingViews = investMapper.findHeroRankingByTradingTime(tradingTime,heroRankingActivityPeriod.get(0),heroRankingActivityPeriod.get(1));

        return CollectionUtils.isNotEmpty(heroRankingViews) && heroRankingViews.size() > 10?heroRankingViews.subList(0,10):heroRankingViews;
    }

    @Override
    public List<HeroRankingView> obtainHeroRankingReferrer(Date tradingTime) {
        return investMapper.findHeroRankingByReferrer(tradingTime,heroRankingActivityPeriod.get(0),heroRankingActivityPeriod.get(1), 0, 10);
    }

    @Override
    public Integer obtainHeroRankingByLoginName(Date tradingTime, final String loginName) {
        if (tradingTime == null) {
            logger.debug("tradingTime is null");
            return null;
        }
        tradingTime = new DateTime(tradingTime).withTimeAtStartOfDay().plusDays(1).minusMillis(1).toDate();
        long count = transferApplicationMapper.findCountTransferApplicationByApplicationTime(loginName, tradingTime,heroRankingActivityPeriod.get(0));
        if (count > 0) {
            return null;
        }
        List<HeroRankingView> heroRankingViews = investMapper.findHeroRankingByTradingTime(tradingTime,heroRankingActivityPeriod.get(0),heroRankingActivityPeriod.get(1));
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

    @Override
    public void saveMysteriousPrize(MysteriousPrizeDto mysteriousPrizeDto) {
        String prizeDate = new DateTime(mysteriousPrizeDto.getPrizeDate()).withTimeAtStartOfDay().toString("yyyy-MM-dd");
        redisWrapperClient.hsetSeri(MYSTERIOUSREDISKEY,prizeDate,mysteriousPrizeDto);
    }

    @Override
    public MysteriousPrizeDto obtainMysteriousPrizeDto(String prizeDate) {
        return (MysteriousPrizeDto)redisWrapperClient.hgetSeri(MYSTERIOUSREDISKEY,prizeDate);
    }

    @Override
    public BaseListDataDto<HeroRankingView> findHeroRankingByReferrer(Date tradingTime, final String loginName, int index, int pageSize) {
        BaseListDataDto<HeroRankingView> baseListDataDto = new BaseListDataDto<>();

        Date activityBeginTime = DateTime.parse(heroRankingActivityPeriod.get(0),DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();

        Date activityEndTime = DateTime.parse(heroRankingActivityPeriod.get(1),DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();

        if (tradingTime.before(activityBeginTime) || tradingTime.after(activityEndTime)) {
            baseListDataDto.setStatus(false);
        } else {
            List<HeroRankingView> heroRankingViewList = investMapper.findHeroRankingByReferrer(tradingTime,heroRankingActivityPeriod.get(0),heroRankingActivityPeriod.get(1), (index - 1) * pageSize, pageSize);
            baseListDataDto.setStatus(true);
            if (CollectionUtils.isNotEmpty(heroRankingViewList)) {
                baseListDataDto.setRecords(Lists.transform(heroRankingViewList, new Function<HeroRankingView, HeroRankingView>() {
                    @Override
                    public HeroRankingView apply(HeroRankingView input) {
                        input.setLoginName(randomUtils.encryptLoginName(loginName, input.getLoginName(), 6));
                        input.setCentSumAmount(input.getCentSumAmount());
                        return input;
                    }
                }));
            }
        }
        return baseListDataDto;
    }

    @Override
    public Integer findHeroRankingByReferrerLoginName(final String loginName) {
        List<HeroRankingView> heroRankingViews = investMapper.findHeroRankingByReferrer(new Date(),heroRankingActivityPeriod.get(0),heroRankingActivityPeriod.get(1), 0, 20);
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

    @Override
    public GivenMembership receiveMembership(String loginName){
        if(DateTime.parse(heroRankingActivityPeriod.get(0), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate().after(DateTime.now().toDate())){
            return GivenMembership.NO_TIME;
        }

        if(DateTime.parse(heroRankingActivityPeriod.get(1),DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate().before(DateTime.now().toDate())){
            return GivenMembership.END_TIME;
        }

        if(loginName == null || loginName.equals("")){
            return GivenMembership.NO_LOGIN;
        }

        if(accountMapper.findByLoginName(loginName) == null){
            return GivenMembership.NO_REGISTER;
        }

        if(userMembershipMapper.findByLoginNameByType(loginName, UserMembershipType.GIVEN) != null){
            return GivenMembership.ALREADY_RECEIVED;
        }

        long investAmount = investMapper.sumSuccessInvestAmountByLoginName(null,loginName);
        Date registerTime = accountMapper.findAccountRegisterTimeByLoginName(loginName);
        if(registerTime != null && DateTime.parse(heroRankingActivityPeriod.get(0),DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate().after(registerTime) && investAmount < 100000){
            return GivenMembership.ALREADY_REGISTER_NOT_INVEST_1000;
        }

        if(registerTime != null && DateTime.parse(heroRankingActivityPeriod.get(0),DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate().after(registerTime) && investAmount >= 100000){
            createUserMembershipModel(loginName, MembershipLevel.V5.getLevel());
            return GivenMembership.ALREADY_REGISTER_ALREADY_INVEST_1000;
        }

        createUserMembershipModel(loginName, MembershipLevel.V5.getLevel());
        return GivenMembership.AFTER_START_ACTIVITY_REGISTER;
    }

    @Override
    public long findUsersCount(){
        return userMapper.findUsersCount();
    }

    @Override
    public long sumInvestAmount(){
        return investMapper.sumInvestAmount(null, null, null, null, null, null, null, InvestStatus.SUCCESS, null);
    }

    private void createUserMembershipModel(String loginName,int level){
        UserMembershipModel userMembershipModel = new UserMembershipModel(loginName,
                membershipMapper.findByLevel(level).getId(),
                DateTime.now().plusMonths(1).toDate(),
                new Date(),
                UserMembershipType.GIVEN);
        userMembershipMapper.create(userMembershipModel);
    }

}
