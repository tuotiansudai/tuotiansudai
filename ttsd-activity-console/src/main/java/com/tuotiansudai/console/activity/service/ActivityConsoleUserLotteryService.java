package com.tuotiansudai.console.activity.service;


import com.google.common.base.Strings;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.tuotiansudai.activity.repository.mapper.ActivityInvestAnnualizedMapper;
import com.tuotiansudai.activity.repository.mapper.ThirdAnniversaryDrawMapper;
import com.tuotiansudai.activity.repository.mapper.UserLotteryPrizeMapper;
import com.tuotiansudai.activity.repository.model.*;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.util.DateUtil;
import com.tuotiansudai.util.RedisWrapperClient;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ActivityConsoleUserLotteryService {

    @Autowired
    private UserLotteryPrizeMapper userLotteryPrizeMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private ActivityCountDrawLotteryService commonCountTimeService;

    @Autowired
    private ThirdAnniversaryDrawMapper thirdAnniversaryDrawMapper;

    @Autowired
    private ActivityInvestAnnualizedMapper activityInvestAnnualizedMapper;

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    private final String THIRD_ANNIVERSARY_EACH_EVERY_DAY_DRAW = "THIRD_ANNIVERSARY_EACH_EVERY_DAY_DRAW:{0}";

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.autumn.startTime}\")}")
    private Date activityAutumnStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.autumn.endTime}\")}")
    private Date activityAutumnEndTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.national.startTime}\")}")
    private Date activityNationalStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.national.endTime}\")}")
    private Date activityNationalEndTime;

    @Value("#{'${activity.carnival.period}'.split('\\~')}")
    private List<String> carnivalTime = Lists.newArrayList();

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.christmas.startTime}\")}")
    private Date activityChristmasStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.christmas.secondStartTime}\")}")
    private Date activityChristmasSecondStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.christmas.endTime}\")}")
    private Date activityChristmasEndTime;

    @Value("#{'${activity.money.tree.period}'.split('\\~')}")
    private List<String> moneyTreeTime = Lists.newArrayList();

    @Value(value = "${activity.exercise.work.startTime}")
    private String acticityExerciseWorkStartTime;

    @Value(value = "${activity.exercise.work.endTime}")
    private String acticityExerciseWorkEndTime;

    public List<UserLotteryTimeView> findUserLotteryTimeViews(String mobile, final ActivityCategory prizeType, Integer page, Integer pageSize) {
        List<UserModel> userModels = userMapper.findUserModelByMobileLike(mobile, page, pageSize);

        Iterator<UserLotteryTimeView> transform = Iterators.transform(userModels.iterator(), input -> {
            UserLotteryTimeView model = new UserLotteryTimeView(input.getMobile(), input.getLoginName());
            model.setUseCount(userLotteryPrizeMapper.findUserLotteryPrizeCountViews(input.getMobile(), null, prizeType, null, null));
            int unUserCount = 0;
            if (prizeType.name().startsWith("MONEY_TREE")) {
                int referrerLotteryChance = commonCountTimeService.countDrawLotteryTime(model.getMobile(), prizeType);
                unUserCount = moneyTreeLeftLotteryTimes(mobile, referrerLotteryChance, model.getUseCount());
            } else if (prizeType.name().startsWith("EXERCISE_WORK_ACTIVITY")) {
                int referrerLotteryChance = commonCountTimeService.countDrawLotteryTime(model.getMobile(), prizeType);
                unUserCount = exerciseVSWorkTimes(model.getMobile(), prizeType) + referrerLotteryChance - model.getUseCount();
            } else if (prizeType == ActivityCategory.THIRD_ANNIVERSARY_ACTIVITY) {
                int useCount = thirdAnniversaryDrawMapper.countDrawByLoginName(input.getLoginName());
                model.setUseCount(useCount);
                unUserCount = this.thirdAnniversaryTimes(input.getLoginName(), useCount);
            } else {
                unUserCount = commonCountTimeService.countDrawLotteryTime(model.getMobile(), prizeType) - model.getUseCount();
            }
            model.setUnUseCount(unUserCount < 0 ? 0 : unUserCount);
            return model;
        });

        return Lists.newArrayList(transform);
    }

    public int findUserLotteryTimeCountViews(String mobile) {
        return userMapper.findCountByMobileLike(mobile);
    }

    public List<UserLotteryPrizeView> findUserLotteryPrizeViews(String mobile, LotteryPrize selectPrize, ActivityCategory prizeType, Date startTime, Date endTime, Integer index, Integer pageSize) {
        return userLotteryPrizeMapper.findUserLotteryPrizeViews(mobile, selectPrize, prizeType, startTime, endTime, index, pageSize);
    }

    public List<UserLotteryPrizeView> findUserLotteryPrizeViewsByHeadlinesToday(String mobile, LotteryPrize selectPrize, ActivityCategory prizeType, Date startTime, Date endTime, Integer index, Integer pageSize, String authenticationType) {
        List<UserLotteryPrizeView> lotteryPrizeViewList = userLotteryPrizeMapper.findUserLotteryPrizeViews(mobile, selectPrize, prizeType, startTime, endTime, index, pageSize);
        //0-代表未实名认证用户 1-代表已实名认证用户
        return lotteryPrizeViewList.stream()
                .filter((UserLotteryPrizeView userLotteryPrizeView) -> isSpecialAuthType(authenticationType, userLotteryPrizeView))
                .map(userLotteryPrizeView -> {
                    userLotteryPrizeView.setInvestFlag(investMapper.sumSuccessInvestCountByLoginName(userLotteryPrizeView.getLoginName()) > 0 ? "是" : "否");
                    return userLotteryPrizeView;
                }).collect(Collectors.toList());
    }

    public boolean isSpecialAuthType(String authenticationType, UserLotteryPrizeView userLotteryPrizeView) {
        switch (authenticationType) {
            case "0":
                return Strings.isNullOrEmpty(userLotteryPrizeView.getUserName());
            case "1":
                return !Strings.isNullOrEmpty(userLotteryPrizeView.getUserName());
        }
        return true;
    }

    public int findUserLotteryPrizeCountViews(String mobile, LotteryPrize selectPrize, ActivityCategory prizeType, Date startTime, Date endTime) {
        return userLotteryPrizeMapper.findUserLotteryPrizeCountViews(mobile, selectPrize, prizeType, startTime, endTime);
    }

    private int moneyTreeLeftLotteryTimes(String mobile, int referrerLotteryTimes, int usedLotteryTimes) {
        int lotteryTimes = 0;
        UserModel userModel = userMapper.findByMobile(mobile);
        if (userModel == null) {
            return 0;
        }

        //判断当天是否参与过摇奖，没有参与过给一次机会
        int isLottery = userLotteryPrizeMapper.findUserLotteryPrizeCountViews(userModel.getMobile(), null, ActivityCategory.MONEY_TREE, new DateTime(new Date()).withTimeAtStartOfDay().toDate(), new DateTime(new Date()).withTimeAtStartOfDay().plusHours(23).plusMinutes(59).plusSeconds(59).toDate());
        if (isLottery == 0) {
            lotteryTimes = 1;
        }

        int usedEveryDayLotteryTimes = 0;
        Date startTime = DateTime.parse(moneyTreeTime.get(0), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        Date endTime = new Date();

        //查询活动开始后，每天的摇奖次数（如果每天都有摇将，则记录每天一次的总和）
        long countDays = DateUtil.differenceDay(startTime, endTime);

        for (int i = 0; i <= countDays; i++) {
            int isEveryDayLottery = userLotteryPrizeMapper.findUserLotteryPrizeCountViews(userModel.getMobile(), null, ActivityCategory.MONEY_TREE, new DateTime(startTime).plusDays(i).withTimeAtStartOfDay().toDate(), new DateTime(startTime).plusDays(i).withTimeAtStartOfDay().plusHours(23).plusMinutes(59).plusSeconds(59).toDate());
            usedEveryDayLotteryTimes += isEveryDayLottery > 0 ? 1 : 0;
        }

        lotteryTimes = (lotteryTimes + referrerLotteryTimes) - (usedLotteryTimes - usedEveryDayLotteryTimes);
        return lotteryTimes;
    }

    private int exerciseVSWorkTimes(String mobile, ActivityCategory activityCategory) {
        final long EACH_INVEST_AMOUNT_100000 = 1000000L;
        DateTime startTime = DateTime.parse(acticityExerciseWorkStartTime, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        UserModel userModel = userMapper.findByMobile(mobile);
        int count = 0;
        Date yesterdayDate = DateTime.now().withTimeAtStartOfDay().minusMillis(1).toDate();
        startTime = startTime.withTimeAtStartOfDay();
        while (startTime.toDate().before(yesterdayDate)) {
            count += userLotteryPrizeMapper.findUserLotteryPrizeCountViews(userModel.getMobile(), null, activityCategory,
                    startTime.toDate(), startTime.plusDays(1).minusMillis(1).toDate()) == 0 ? 0 : 1;
            startTime = startTime.plusDays(1);
        }
        return count + 1;
    }

    private int thirdAnniversaryTimes(String loginName, int useCount) {
        final long EACH_INVEST_AMOUNT_50000 = 50000l;

        ActivityInvestAnnualizedModel activityInvestAnnualizedModel = activityInvestAnnualizedMapper.findByActivityAndLoginName(ActivityInvestAnnualized.THIRD_ANNIVERSARY_ACTIVITY, loginName);
        int investDrawCount = activityInvestAnnualizedModel == null ? 0 : (int) (activityInvestAnnualizedModel.getSumInvestAmount() / EACH_INVEST_AMOUNT_50000);

        Map<String, String> eachEveryDayDraws = redisWrapperClient.hgetAll(MessageFormat.format(THIRD_ANNIVERSARY_EACH_EVERY_DAY_DRAW, loginName));
        int usedInvestDrawCount = useCount - eachEveryDayDraws.size();

        int isTodayDraw = eachEveryDayDraws.entrySet().stream().filter(entry -> entry.getKey().equals(DateTime.now().toString("yyyy-MM-dd"))).count() > 0 ? 0 : 1;
        return investDrawCount - usedInvestDrawCount + isTodayDraw;
    }

}
