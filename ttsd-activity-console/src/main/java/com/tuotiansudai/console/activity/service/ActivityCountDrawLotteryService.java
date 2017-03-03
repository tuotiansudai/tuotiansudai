package com.tuotiansudai.console.activity.service;


import com.google.common.collect.Lists;
import com.tuotiansudai.activity.repository.model.ActivityCategory;
import com.tuotiansudai.activity.repository.model.ActivityDrawLotteryTask;
import com.tuotiansudai.point.repository.mapper.PointBillMapper;
import com.tuotiansudai.point.repository.model.PointBusinessType;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service
public class ActivityCountDrawLotteryService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private BankCardMapper bankCardMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private RechargeMapper rechargeMapper;

    @Autowired
    private PointBillMapper pointBillMapper;

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

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.woman.day.startTime}\")}")
    private Date activityWomanDayStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.woman.day.endTime}\")}")
    private Date activityWomanDayEndTime;

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

    public static final String ACTIVITY_DESCRIPTION = "新年专享";

    //每投资5000奖励抽奖次数
    private final long EACH_INVEST_AMOUNT_50000 = 500000L;

    private final long EACH_INVEST_AMOUNT_20000 = 200000L;


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
                return countDrawLotteryTime(userModel,activityCategory,Lists.newArrayList(ActivityDrawLotteryTask.EACH_INVEST_1000));
            case SPRING_FESTIVAL_ACTIVITY:
                return countDrawLotteryTime(userModel, activityCategory, springFestivalActivityTasks);
            case WOMAN_DAY_ACTIVITY:
                return countDrawLotteryTime(userModel, activityCategory, Lists.newArrayList(ActivityDrawLotteryTask.TODAY_ACTIVITY_SIGN_IN));
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
                    List<UserModel> userModels = userMapper.findUsersByRegisterTimeOrReferrer(startTime, endTime, userModel.getLoginName());
                    for (UserModel referrerUserModel : userModels) {
                        if (referrerUserModel.getRegisterTime().before(endTime) && referrerUserModel.getRegisterTime().after(startTime)) {
                            time++;
                        }
                    }
                    break;
                case EACH_REFERRER_INVEST:
                    List<UserModel> referrerUserModels = userMapper.findUsersByRegisterTimeOrReferrer(startTime, endTime, userModel.getLoginName());
                    for (UserModel referrerUserModel : referrerUserModels) {
                        if (investMapper.countInvestorSuccessInvestByInvestTime(referrerUserModel.getLoginName(), startTime, endTime) > 0) {
                            time++;
                        }
                    }
                    break;
                case CERTIFICATION:
                    AccountModel accountModel = accountMapper.findByLoginName(userModel.getLoginName());
                    if (accountModel != null && accountModel.getRegisterTime().before(endTime) && accountModel.getRegisterTime().after(startTime)) {
                        time++;
                    }
                    break;
                case BANK_CARD:
                    BankCardModel bankCardModel = bankCardMapper.findPassedBankCardByLoginName(userModel.getLoginName());
                    if (bankCardModel != null && bankCardModel.getCreatedTime().before(endTime) && bankCardModel.getCreatedTime().after(startTime)) {
                        time++;
                    }
                    break;
                case RECHARGE:
                    if (rechargeMapper.findRechargeCount(null, userModel.getMobile(), null, RechargeStatus.SUCCESS, null, startTime, endTime, null) > 0) {
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
                    List<UserModel> referrerUsers = userMapper.findUsersByRegisterTimeOrReferrer(startTime, endTime, userModel.getLoginName());
                    time += referrerUsers.size() * 5;
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
                    time = investMapper.sumDrawCountByLoginName(userModel.getLoginName(),startTime,endTime,100000);
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
                return Lists.newArrayList(DateTime.parse(lanternFestivalStartTime,DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate(),
                        DateTime.parse(lanternFestivalEndTime,DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate());
            case WOMAN_DAY_ACTIVITY:
                return Lists.newArrayList(activityWomanDayStartTime, activityWomanDayEndTime);
        }
        return null;
    }

}
