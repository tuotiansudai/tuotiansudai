package com.tuotiansudai.console.activity.service;


import com.google.common.collect.Lists;
import com.tuotiansudai.activity.repository.model.ActivityCategory;
import com.tuotiansudai.activity.repository.model.ActivityDrawLotteryTask;
import com.tuotiansudai.client.RedisWrapperClient;
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

    @Autowired
    private RedisWrapperClient redisWrapperClient;

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

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.christmas.endTime}\")}")
    private Date activityChristmasEndTime;

    private static final String redisKey = "web:christmasTime:lottery:startTime";

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

    private final String activityDescription = "新年专享";
    //每投资5000奖励抽奖次数
    private final long EACH_INVEST_AMOUNT = 500000;


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
                    if (rechargeMapper.findRechargeCount(null, userModel.getMobile(), null, RechargeStatus.SUCCESS, null, startTime, endTime) > 0) {
                        time++;
                    }
                    break;
                case INVEST:
                    if (investMapper.countInvestorSuccessInvestByInvestTime(userModel.getLoginName(), startTime, endTime) > 0) {
                        time++;
                    }
                    break;
                case EACH_ACTIVITY_SIGN_IN:
                    time += pointBillMapper.findCountPointBillPagination(userModel.getLoginName(), startTime, endTime, Lists.newArrayList(PointBusinessType.SIGN_IN));
                    break;
                case REFERRER_USER:
                    List<UserModel> referrerUsers = userMapper.findUsersByRegisterTimeOrReferrer(startTime, endTime, userModel.getLoginName());
                    time += referrerUsers.size() * 5;
                    break;
                case EACH_INVEST_5000:
                    long sumInvestAmount = investMapper.sumSuccessActivityInvestAmount(userModel.getLoginName(), activityDescription, startTime, endTime);
                    long investAwardTime = sumInvestAmount / EACH_INVEST_AMOUNT;
                    if (investAwardTime <= 10) {
                        time += investAwardTime;
                    }
                    break;
                case EACH_INVEST_2000:
                    long sumAmount = investMapper.sumInvestAmountByLoginNameInvestTimeProductType(userModel.getLoginName(), startTime, endTime, Lists.newArrayList(ProductType._90, ProductType._180, ProductType._360));
                    time += (int) (sumAmount / 200000);
                    time = time >= 10 ? 10 : time;
                    break;
                case FIRST_INVEST:
                    boolean beforeIsInvest = investMapper.sumInvestAmountByLoginNameInvestTimeProductType(userModel.getLoginName(), new DateTime().minusDays(720).toDate(), activityChristmasStartTime, null) > 0;
                    boolean currentIsInvest = investMapper.sumInvestAmountByLoginNameInvestTimeProductType(userModel.getLoginName(), activityChristmasStartTime, activityChristmasEndTime, null)  > 0 ;

                    if(!beforeIsInvest && currentIsInvest){
                        time++;
                    }
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
                return Lists.newArrayList(redisWrapperClient.exists(redisKey) ? DateTime.parse(redisWrapperClient.get(redisKey), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate() : activityChristmasEndTime, activityChristmasEndTime);
        }
        return null;
    }

}
