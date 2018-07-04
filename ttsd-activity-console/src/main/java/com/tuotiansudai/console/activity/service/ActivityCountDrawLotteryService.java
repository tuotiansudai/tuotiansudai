package com.tuotiansudai.console.activity.service;


import com.google.common.collect.Lists;
import com.tuotiansudai.activity.repository.mapper.UserLotteryPrizeMapper;
import com.tuotiansudai.activity.repository.model.ActivityCategory;
import com.tuotiansudai.activity.repository.model.ActivityDrawLotteryTask;
import com.tuotiansudai.enums.BankRechargeStatus;
import com.tuotiansudai.point.repository.mapper.PointBillMapper;
import com.tuotiansudai.point.repository.model.PointBusinessType;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.util.RedisWrapperClient;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class ActivityCountDrawLotteryService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BankAccountMapper bankAccountMapper;

    @Autowired
    private UserBankCardMapper userBankCardMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private BankRechargeMapper bankRechargeMapper;

    @Autowired
    private PointBillMapper pointBillMapper;

    @Autowired
    private UserLotteryPrizeMapper userLotteryPrizeMapper;

    private RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    public final static String ACTIVITY_DOUBLE_ELEVEN_INVEST_KEY = "activity:double:eleven:invest";

    public final static String ACTIVITY_DOUBLE_ELEVEN_USER_INVEST_COUNT_KEY = "activity:double:eleven:user:invest:count";

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

    @Value("#{'${activity.annual.period}'.split('\\~')}")
    private List<String> annualTime = Lists.newArrayList();

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.christmas.startTime}\")}")
    private Date activityChristmasStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.christmas.secondStartTime}\")}")
    private Date activityChristmasSecondStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.christmas.endTime}\")}")
    private Date activityChristmasEndTime;

    @Value(value = "${activity.lanternFestival.startTime}")
    private String lanternFestivalStartTime;
    @Value(value = "${activity.lanternFestival.endTime}")
    private String lanternFestivalEndTime;

    @Value("#{'${activity.money.tree.period}'.split('\\~')}")
    private List<String> moneyTreeTime = Lists.newArrayList();

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.woman.day.startTime}\")}")
    private Date activityWomanDayStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.woman.day.endTime}\")}")
    private Date activityWomanDayEndTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.celebration.single.startTime}\")}")
    private Date activitySingleStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.celebration.single.endTime}\")}")
    private Date activitySingleEndTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.exercise.work.startTime}\")}")
    private Date acticityExerciseWorkStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.exercise.work.endTime}\")}")
    private Date acticityExerciseWorkEndTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.school.season.startTime}\")}")
    private Date acticitySchoolSeasonStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.school.season.endTime}\")}")
    private Date acticitySchoolSeasonEndTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.iphoneX.startTime}\")}")
    private Date activityIphoneXStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.iphoneX.endTime}\")}")
    private Date activityIphoneXEndTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.double.eleven.startTime}\")}")
    private Date activityDoubleElevenStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.double.eleven.endTime}\")}")
    private Date activityDoubleElevenEndTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.year.end.awards.startTime}\")}")
    private Date activityYearEndAwardsStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.year.end.awards.endTime}\")}")
    private Date activityYearEndAwardsEndTime;

    //往期活动任务
    private final List activityTasks = Lists.newArrayList(ActivityDrawLotteryTask.REGISTER, ActivityDrawLotteryTask.EACH_REFERRER,
            ActivityDrawLotteryTask.EACH_REFERRER_INVEST, ActivityDrawLotteryTask.CERTIFICATION, ActivityDrawLotteryTask.BANK_CARD,
            ActivityDrawLotteryTask.RECHARGE, ActivityDrawLotteryTask.INVEST);

    //圣诞活动活动任务
    private final List christmasTasks = Lists.newArrayList(ActivityDrawLotteryTask.REGISTER, ActivityDrawLotteryTask.EACH_REFERRER,
            ActivityDrawLotteryTask.EACH_REFERRER_INVEST, ActivityDrawLotteryTask.CERTIFICATION, ActivityDrawLotteryTask.INVEST,
            ActivityDrawLotteryTask.EACH_INVEST_2000, ActivityDrawLotteryTask.FIRST_INVEST);

    //元旦活动任务
    private final List newYearsActivityTask = Lists.newArrayList(ActivityDrawLotteryTask.EACH_ACTIVITY_SIGN_IN, ActivityDrawLotteryTask.REFERRER_USER,
            ActivityDrawLotteryTask.EACH_INVEST_5000);

    //春节活动任务
    private final List springFestivalActivityTasks = Lists.newArrayList(ActivityDrawLotteryTask.EACH_ACTIVITY_SIGN_IN);

    //摇钱树活动任务
    private final List moneyTreeActivityTasks = Lists.newArrayList(ActivityDrawLotteryTask.EACH_REFERRER);

    public static final String ACTIVITY_DESCRIPTION = "新年专享";

    //每投资5000奖励抽奖次数
    private final long EACH_INVEST_AMOUNT_50000 = 500000L;

    private final long EACH_INVEST_AMOUNT_20000 = 200000L;

    private final long EACH_INVEST_AMOUNT_100000 = 1000000L;


    public int countDrawLotteryTime(String mobile, ActivityCategory activityCategory) {
        int lotteryTime = 0;
        UserModel userModel = userMapper.findByMobile(mobile);
        if (userModel == null) return lotteryTime;

        switch (activityCategory) {
            case AUTUMN_PRIZE:
            case NATIONAL_PRIZE:
            case CARNIVAL_ACTIVITY:
                return countDrawLotteryTime(userModel, activityCategory, activityTasks);
            case ANNUAL_ACTIVITY:
                return countDrawLotteryTime(userModel, activityCategory, newYearsActivityTask);
            case CHRISTMAS_ACTIVITY:
                return countDrawLotteryTime(userModel, activityCategory, christmasTasks);
            case LANTERN_FESTIVAL_ACTIVITY:
                return countDrawLotteryTime(userModel, activityCategory, Lists.newArrayList(ActivityDrawLotteryTask.EACH_INVEST_1000));
            case SPRING_FESTIVAL_ACTIVITY:
                return countDrawLotteryTime(userModel, activityCategory, springFestivalActivityTasks);
            case MONEY_TREE:
                return countDrawLotteryTime(userModel, activityCategory, moneyTreeActivityTasks);
            case WOMAN_DAY_ACTIVITY:
                return countDrawLotteryTime(userModel, activityCategory, Lists.newArrayList(ActivityDrawLotteryTask.TODAY_ACTIVITY_SIGN_IN));
            case CELEBRATION_SINGLE_ACTIVITY:
                return countDrawLotteryTime(userModel, activityCategory, Lists.newArrayList(ActivityDrawLotteryTask.EACH_INVEST_10000));
            case EXERCISE_WORK_ACTIVITY:
                return countDrawLotteryTime(userModel, activityCategory, Lists.newArrayList(ActivityDrawLotteryTask.EACH_INVEST_10000));
            case SCHOOL_SEASON_ACTIVITY:
                return countDrawLotteryTime(userModel, activityCategory, Lists.newArrayList(ActivityDrawLotteryTask.EACH_EVERY_DAY));
            case IPHONEX_ACTIVITY:
                return countDrawLotteryTime(userModel, activityCategory, Lists.newArrayList(ActivityDrawLotteryTask.EACH_INVEST_10000));
            case DOUBLE_ELEVEN_ACTIVITY:
                return countDrawLotteryTime(userModel, activityCategory, Lists.newArrayList(ActivityDrawLotteryTask.DOUBLE_ELEVEN_INVEST));
            case YEAR_END_AWARDS_ACTIVITY:
                return countDrawLotteryTime(userModel, activityCategory, Lists.newArrayList(ActivityDrawLotteryTask.EACH_EVERY_DAY));
        }
        return lotteryTime;
    }

    private int countDrawLotteryTime(UserModel userModel, ActivityCategory activityCategory, List<ActivityDrawLotteryTask> activityDrawLotteryTasks) {
        int time = 0;
        List<Date> activityDate = getActivityDate(activityCategory);
        Date startTime = activityDate.get(0);
        Date endTime = activityDate.get(1);

        for (ActivityDrawLotteryTask activityDrawLotteryTask : activityDrawLotteryTasks) {
            switch (activityDrawLotteryTask) {
                case REGISTER:
                    if (userModel.getRegisterTime().before(endTime) && userModel.getRegisterTime().after(startTime)) {
                        time++;
                    }
                    break;
                case EACH_REFERRER:
                    List<UserRegisterInfo> userModels = userMapper.findAllUsersByRegisterTimeAndReferrer(startTime, endTime, userModel.getLoginName());
                    if (activityCategory.name().startsWith("MONEY_TREE")) {
                        //根据注册时间分组
                        Map<String, Long> groupByEveryDayCounts = userModels
                                .stream()
                                .collect(Collectors.groupingBy(p -> String.format("%tF", p.getRegisterTime()), Collectors.counting()));

                        //单日邀请人数超过3人者，最多给3次摇奖机会
                        for (Map.Entry<String, Long> entry : groupByEveryDayCounts.entrySet()) {
                            if (entry.getValue() > 3) {
                                time += 3;
                            } else {
                                time += entry.getValue();
                            }
                        }
                    } else {
                        for (UserRegisterInfo referrerUserModel : userModels) {
                            if (referrerUserModel.getRegisterTime().before(endTime) && referrerUserModel.getRegisterTime().after(startTime)) {
                                time++;
                            }
                        }
                    }
                    break;
                case EACH_REFERRER_INVEST:
                    List<UserRegisterInfo> referrerUserModels = userMapper.findAllUsersByRegisterTimeAndReferrer(startTime, endTime, userModel.getLoginName());
                    for (UserRegisterInfo referrerUserModel : referrerUserModels) {
                        if (investMapper.countInvestorSuccessInvestByInvestTime(referrerUserModel.getLoginName(), startTime, endTime) > 0) {
                            time++;
                        }
                    }
                    break;
                case CERTIFICATION:
                    BankAccountModel bankAccountModel = bankAccountMapper.findInvestorByLoginName(userModel.getLoginName());
                    if (bankAccountModel != null && bankAccountModel.getCreatedTime().before(endTime) && bankAccountModel.getCreatedTime().after(startTime)) {
                        time++;
                    }
                    break;
                case BANK_CARD:
                    UserBankCardModel userBankCardModel = userBankCardMapper.findByLoginName(userModel.getLoginName());
                    if (userBankCardModel != null && userBankCardModel.getCreatedTime().before(endTime) && userBankCardModel.getCreatedTime().after(startTime)) {
                        time++;
                    }
                    break;
                case RECHARGE:
                    if (bankRechargeMapper.findRechargeCount(null, userModel.getMobile(), null, BankRechargeStatus.SUCCESS, null, startTime, endTime, null) > 0) {
                        time++;
                    }
                    break;
                case INVEST:
                    if (investMapper.countInvestorSuccessInvestByInvestTime(userModel.getLoginName(), startTime, endTime) > 0) {
                        time++;
                    }
                    break;
                case EACH_ACTIVITY_SIGN_IN:
                    time += pointBillMapper.findCountPointBillPagination(userModel.getLoginName(), null, startTime, endTime, Lists.newArrayList(PointBusinessType.SIGN_IN));
                    break;
                case REFERRER_USER:
                    long referrerUserCount = userMapper.findUserCountByRegisterTimeAndReferrer(startTime, endTime, userModel.getLoginName());
                    time += referrerUserCount * 5;
                    break;
                case EACH_INVEST_5000:
                    long sumInvestAmount = investMapper.sumSuccessActivityInvestAmount(userModel.getLoginName(), ACTIVITY_DESCRIPTION, startTime, endTime);
                    long investAwardTime = sumInvestAmount / EACH_INVEST_AMOUNT_50000;
                    if (investAwardTime <= 10) {
                        time += investAwardTime;
                    } else {
                        time += 10;
                    }
                    break;
                case EACH_INVEST_2000:
                    long sumAmount = investMapper.sumInvestAmountByLoginNameInvestTimeProductType(userModel.getLoginName(), startTime, endTime, Lists.newArrayList(ProductType._90, ProductType._180, ProductType._360));
                    time += (int) (sumAmount / EACH_INVEST_AMOUNT_20000);
                    time = time >= 10 ? 10 : time;
                    break;
                case EACH_INVEST_1000:
                    time = investMapper.sumDrawCountByLoginName(userModel.getLoginName(), startTime, endTime, 100000);
                    break;
                case EACH_INVEST_10000:
                    List<InvestModel> investModels = investMapper.findSuccessByLoginNameExceptTransferAndTime(userModel.getLoginName(), startTime, endTime);
                    for (InvestModel investModel : investModels) {
                        time += investModel.getAmount() < EACH_INVEST_AMOUNT_100000 ? 0 : (int) (investModel.getAmount() / EACH_INVEST_AMOUNT_100000);
                    }
                    break;
                case EACH_EVERY_DAY:
                    time = userLotteryPrizeMapper.findUserLotteryPrizeCountViews(userModel.getMobile(), null, activityCategory,
                            DateTime.now().withTimeAtStartOfDay().toDate(), DateTime.now().plusDays(1).withTimeAtStartOfDay().plusMillis(-1).toDate()) == 0 ? 1 : 0;
                    break;
                case DOUBLE_ELEVEN_INVEST:
                    time = getDoubleElevenDrawTimes(userModel);
                    break;
            }
        }
        return time;
    }


    private List<Date> getActivityDate(ActivityCategory activityCategory) {
        switch (activityCategory) {
            case AUTUMN_PRIZE:
                return Lists.newArrayList(activityAutumnStartTime, activityAutumnEndTime);
            case NATIONAL_PRIZE:
                return Lists.newArrayList(activityNationalStartTime, activityNationalEndTime);
            case CARNIVAL_ACTIVITY:
                return Lists.newArrayList(DateTime.parse(carnivalTime.get(0), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate(), DateTime.parse(carnivalTime.get(1), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate());
            case ANNUAL_ACTIVITY:
                return Lists.newArrayList(DateTime.parse(annualTime.get(0), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate(), DateTime.parse(annualTime.get(1), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate());
            case CHRISTMAS_ACTIVITY:
                return Lists.newArrayList(activityChristmasSecondStartTime, activityChristmasEndTime);
            case LANTERN_FESTIVAL_ACTIVITY:
                return Lists.newArrayList(DateTime.parse(lanternFestivalStartTime, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate(),
                        DateTime.parse(lanternFestivalEndTime, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate());
            case MONEY_TREE:
            case MONEY_TREE_UNDER_1000_ACTIVITY:
            case MONEY_TREE_UNDER_10000_ACTIVITY:
            case MONEY_TREE_UNDER_20000_ACTIVITY:
            case MONEY_TREE_UNDER_30000_ACTIVITY:
            case MONEY_TREE_UNDER_40000_ACTIVITY:
            case MONEY_TREE_UNDER_50000_ACTIVITY:
            case MONEY_TREE_UNDER_60000_ACTIVITY:
            case MONEY_TREE_UNDER_70000_ACTIVITY:
            case MONEY_TREE_UNDER_80000_ACTIVITY:
            case MONEY_TREE_UNDER_90000_ACTIVITY:
            case MONEY_TREE_UNDER_100000_ACTIVITY:
            case MONEY_TREE_ABOVE_100000_ACTIVITY:
                return Lists.newArrayList(DateTime.parse(moneyTreeTime.get(0), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate(), DateTime.parse(moneyTreeTime.get(1), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate());
            case WOMAN_DAY_ACTIVITY:
                return Lists.newArrayList(activityWomanDayStartTime, activityWomanDayEndTime);
            case CELEBRATION_SINGLE_ACTIVITY:
                return Lists.newArrayList(activitySingleStartTime, activitySingleEndTime);
            case EXERCISE_WORK_ACTIVITY:
                return Lists.newArrayList(acticityExerciseWorkStartTime, acticityExerciseWorkEndTime);
            case SCHOOL_SEASON_ACTIVITY:
                return Lists.newArrayList(acticitySchoolSeasonStartTime, acticityExerciseWorkEndTime);
            case IPHONEX_ACTIVITY:
                return Lists.newArrayList(activityIphoneXStartTime, activityIphoneXEndTime);
            case DOUBLE_ELEVEN_ACTIVITY:
                return Lists.newArrayList(activityDoubleElevenStartTime, activityDoubleElevenEndTime);
            case YEAR_END_AWARDS_ACTIVITY:
                return Lists.newArrayList(activityYearEndAwardsStartTime, activityYearEndAwardsEndTime);
        }
        return null;
    }

    private int getDoubleElevenDrawTimes(UserModel userModel) {
        int investDrawTimes = 0;
        List<InvestModel> investModels = investMapper.findSuccessDoubleElevenActivityByTime(null, activityDoubleElevenStartTime, activityDoubleElevenEndTime);

        redisWrapperClient.del(ACTIVITY_DOUBLE_ELEVEN_USER_INVEST_COUNT_KEY);
        int count = 0;
        for (InvestModel investModel : investModels) {
            String hkey = MessageFormat.format("{0}:{1}:{2}", investModel.getLoanId(), investModel.getId(), userModel.getLoginName());
            String incrKey = MessageFormat.format("{0}:{1}", userModel.getLoginName(), new DateTime(investModel.getTradingTime()).withTimeAtStartOfDay().toString("yyyy-MM-dd"));
            boolean even = String.valueOf(redisWrapperClient.hget(ACTIVITY_DOUBLE_ELEVEN_INVEST_KEY, hkey)).equals("0");
            boolean booleanEvenOfDay = redisWrapperClient.hget(ACTIVITY_DOUBLE_ELEVEN_USER_INVEST_COUNT_KEY, incrKey) != null;
            long evenCountOfDay = booleanEvenOfDay ? Long.parseLong(redisWrapperClient.hget(ACTIVITY_DOUBLE_ELEVEN_USER_INVEST_COUNT_KEY, incrKey)) : 0;
            if (even && evenCountOfDay < 10) {
                if(booleanEvenOfDay){
                    count++;
                }else{
                    count = 1;
                }
                redisWrapperClient.hset(ACTIVITY_DOUBLE_ELEVEN_USER_INVEST_COUNT_KEY, incrKey, String.valueOf(count));
                investDrawTimes++;
            }
        }

        return investDrawTimes;
    }

}
