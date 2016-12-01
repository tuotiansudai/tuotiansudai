package com.tuotiansudai.console.activity.service;


import com.google.common.collect.Lists;
import com.tuotiansudai.activity.repository.model.ActivityCategory;
import com.tuotiansudai.activity.repository.model.ActivityDrawLotteryTask;
import com.tuotiansudai.point.repository.mapper.PointBillMapper;
import com.tuotiansudai.point.repository.model.PointBusinessType;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.BankCardModel;
import com.tuotiansudai.repository.model.RechargeStatus;
import com.tuotiansudai.repository.model.UserModel;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service
public abstract class CommonCountTimeService {

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

    @Value("#{'${activity.new.years.period}'.split('\\~')}")
    private List<String> newYearsTime = Lists.newArrayList();

    List activityTasks = Lists.newArrayList(ActivityDrawLotteryTask.REGISTER, ActivityDrawLotteryTask.EACH_REFERRER, ActivityDrawLotteryTask.EACH_REFERRER_INVEST, ActivityDrawLotteryTask.CERTIFICATION, ActivityDrawLotteryTask.BANK_CARD, ActivityDrawLotteryTask.RECHARGE, ActivityDrawLotteryTask.INVEST);
    List newYearsActivityTask = Lists.newArrayList(ActivityDrawLotteryTask.EACH_ACTIVITY_SIGN_IN, ActivityDrawLotteryTask.REFERRER_USER, ActivityDrawLotteryTask.EACH_INVEST_5000);


    public int countDrawLotteryTime(String mobile, ActivityCategory activityCategory) {
        int lotteryTime = 0;
        UserModel userModel = userMapper.findByMobile(mobile);
        if (userModel == null) return lotteryTime;

        switch (activityCategory) {
            case AUTUMN_PRIZE:
            case NATIONAL_PRIZE:
            case CARNIVAL_ACTIVITY:
                return countDrawLotteryTime(userModel, activityCategory, activityTasks);
            case NEW_YEARS_DAY_ACTIVITY:
                return countDrawLotteryTime(userModel, activityCategory, newYearsActivityTask);
        }
        return lotteryTime;
    }

    private int countDrawLotteryTime(UserModel userModel, ActivityCategory activityCategory, List<ActivityDrawLotteryTask> activityDrawLotteryTasks) {
        int time = 0;
        Date startTime = getActivityStartTime(activityCategory);
        Date endTime = getActivityEndTime(activityCategory);

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

                    break;

            }
        }

        return time;
    }


    private Date getActivityStartTime(ActivityCategory activityCategory) {
        switch (activityCategory) {
            case AUTUMN_PRIZE:
                return activityAutumnStartTime;
            case NATIONAL_PRIZE:
                return activityNationalStartTime;
            case CARNIVAL_ACTIVITY:
                return DateTime.parse(carnivalTime.get(0), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
            case NEW_YEARS_DAY_ACTIVITY:
                return DateTime.parse(newYearsTime.get(0), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        }
        return null;
    }

    private Date getActivityEndTime(ActivityCategory activityCategory) {
        switch (activityCategory) {
            case AUTUMN_PRIZE:
                return activityAutumnEndTime;
            case NATIONAL_PRIZE:
                return activityNationalEndTime;
            case CARNIVAL_ACTIVITY:
                return DateTime.parse(carnivalTime.get(1), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
            case NEW_YEARS_DAY_ACTIVITY:
                return DateTime.parse(newYearsTime.get(1), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        }
        return null;
    }

}
