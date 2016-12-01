package com.tuotiansudai.console.activity.service;


import com.google.common.collect.Lists;
import com.tuotiansudai.activity.repository.model.ActivityCategory;
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


    public int countDrawLotteryTime(String mobile, ActivityCategory activityCategory) {
        int lotteryTime = 0;
        UserModel userModel = userMapper.findByMobile(mobile);

        if (userModel == null) return lotteryTime;

        Date startTime = getActivityStartTime(activityCategory);
        Date endTime = getActivityEndTime(activityCategory);


        switch (activityCategory){
            case AUTUMN_PRIZE:
            case NATIONAL_PRIZE:
            case CARNIVAL_ACTIVITY:
                break;
            case NEW_YEARS_DAY_ACTIVITY:
                break;
        }

        List<UserModel> userModels = userMapper.findUsersByRegisterTimeOrReferrer(startTime, endTime, userModel.getLoginName());
        for (UserModel referrerUserModel : userModels) {
            if (referrerUserModel.getRegisterTime().before(endTime) && referrerUserModel.getRegisterTime().after(startTime)) {
                lotteryTime++;
                if (investMapper.countInvestorSuccessInvestByInvestTime(referrerUserModel.getLoginName(), startTime, endTime) > 0) {
                    lotteryTime++;
                }
            }
        }

        if (userModel.getRegisterTime().before(endTime) && userModel.getRegisterTime().after(startTime)) {
            lotteryTime++;
        }

        AccountModel accountModel = accountMapper.findByLoginName(userModel.getLoginName());
        if (accountModel != null && accountModel.getRegisterTime().before(endTime) && accountModel.getRegisterTime().after(startTime)) {
            lotteryTime++;
        }

        BankCardModel bankCardModel = bankCardMapper.findPassedBankCardByLoginName(userModel.getLoginName());
        if (bankCardModel != null && bankCardModel.getCreatedTime().before(endTime) && bankCardModel.getCreatedTime().after(startTime)) {
            lotteryTime++;
        }

        if (rechargeMapper.findRechargeCount(null, userModel.getMobile(), null, RechargeStatus.SUCCESS, null, startTime, endTime) > 0) {
            lotteryTime++;
        }

        if (investMapper.countInvestorSuccessInvestByInvestTime(userModel.getLoginName(), startTime, endTime) > 0) {
            lotteryTime++;
        }

        return lotteryTime;
    }


    protected Date getActivityStartTime(ActivityCategory activityCategory){
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

    protected Date getActivityEndTime(ActivityCategory activityCategory){
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
