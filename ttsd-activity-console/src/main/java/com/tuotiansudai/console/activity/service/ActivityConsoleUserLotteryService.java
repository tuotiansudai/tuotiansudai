package com.tuotiansudai.console.activity.service;


import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.tuotiansudai.activity.repository.model.ActivityCategory;
import com.tuotiansudai.activity.repository.model.LotteryPrize;
import com.tuotiansudai.activity.repository.model.UserLotteryPrizeView;
import com.tuotiansudai.activity.repository.model.UserLotteryTimeView;
import com.tuotiansudai.activity.repository.mapper.UserLotteryPrizeMapper;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Service
public class ActivityConsoleUserLotteryService {

    @Autowired
    private UserLotteryPrizeMapper userLotteryPrizeMapper;

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

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.christmas.startTime}\")}")
    private Date activityChristmasStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.christmas.endTime}\")}")
    private Date  activityChristmasEndTime;

    private static final String redisKey = "web:christmasTime:lottery:startTime";

    public List<UserLotteryTimeView> findUserLotteryTimeViews(String mobile,final ActivityCategory prizeType,Integer index,Integer pageSize) {
        List<UserModel> userModels = userMapper.findUserModelByMobile(mobile,index, pageSize);

        Iterator<UserLotteryTimeView> transform = Iterators.transform(userModels.iterator(), input -> {
            UserLotteryTimeView model = new UserLotteryTimeView(input.getMobile(),input.getLoginName());
            model.setUseCount(userLotteryPrizeMapper.findUserLotteryPrizeCountViews(input.getMobile(), null,prizeType, null, null));
            int lotteryTimes = findLotteryTime(model.getMobile(),prizeType);
            model.setUnUseCount((lotteryTimes - model.getUseCount()) < 0 ? 0 : (lotteryTimes - model.getUseCount()) );
            return model;
        });

        return Lists.newArrayList(transform);
    }

    public int findUserLotteryTimeCountViews(String mobile){
        return userMapper.findUserModelByMobile(mobile,null, null).size();
    }

    public List<UserLotteryPrizeView> findUserLotteryPrizeViews(String mobile,LotteryPrize selectPrize,ActivityCategory prizeType,Date startTime,Date endTime,Integer index,Integer pageSize){
        return userLotteryPrizeMapper.findUserLotteryPrizeViews(mobile, selectPrize, prizeType, startTime, endTime, index, pageSize);
    }

    public int findUserLotteryPrizeCountViews(String mobile,LotteryPrize selectPrize,ActivityCategory prizeType,Date startTime,Date endTime){
        return userLotteryPrizeMapper.findUserLotteryPrizeCountViews(mobile, selectPrize, prizeType, startTime, endTime);
    }

    private int findLotteryTime(String mobile,ActivityCategory activityCategory){
        int lotteryTime = 0;
        UserModel userModel = userMapper.findByMobile(mobile);
        if(userModel == null){
            return lotteryTime;
        }

        Date startTime = null;
        Date endTime = null;
        switch (activityCategory){
            case AUTUMN_PRIZE:
                startTime = activityAutumnStartTime;
                endTime = activityAutumnEndTime;
                return findLotteryTimeByAutumnAndNationalAndCarnival(userModel, startTime, endTime);
            case NATIONAL_PRIZE:
                startTime = activityNationalStartTime;
                endTime = activityNationalEndTime;
                return findLotteryTimeByAutumnAndNationalAndCarnival(userModel, startTime, endTime);
            case CARNIVAL_ACTIVITY:
                startTime = DateTime.parse(carnivalTime.get(0), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
                endTime = DateTime.parse(carnivalTime.get(1), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
                return findLotteryTimeByAutumnAndNationalAndCarnival(userModel, startTime, endTime);
            case CHRISTMAS_ACTIVITY:
                startTime = redisWrapperClient.exists(redisKey) ? DateTime.parse(redisWrapperClient.get(redisKey), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate() : activityChristmasEndTime;
                endTime = activityChristmasEndTime;
                return findLotteryTimeByChristmas(userModel, startTime, endTime);
        }
        return lotteryTime;
    }

    private int findLotteryTimeByAutumnAndNationalAndCarnival(UserModel userModel, Date startTime, Date endTime){
        int lotteryTime = 0;

        List<UserModel> userModels = userMapper.findUsersByRegisterTimeOrReferrer(startTime,endTime, userModel.getLoginName());
        for(UserModel referrerUserModel : userModels){
            if(referrerUserModel.getRegisterTime().before(endTime) && referrerUserModel.getRegisterTime().after(startTime)){
                lotteryTime ++;
                if(investMapper.countInvestorSuccessInvestByInvestTime(referrerUserModel.getLoginName(),startTime,endTime) > 0){
                    lotteryTime ++;
                }
            }
        }

        if(userModel.getRegisterTime().before(endTime) && userModel.getRegisterTime().after(startTime)){
            lotteryTime ++;
        }

        AccountModel accountModel = accountMapper.findByLoginName(userModel.getLoginName());
        if(accountModel != null && accountModel.getRegisterTime().before(endTime) && accountModel.getRegisterTime().after(startTime)){
            lotteryTime ++;
        }

        BankCardModel bankCardModel = bankCardMapper.findPassedBankCardByLoginName(userModel.getLoginName());
        if(bankCardModel != null && bankCardModel.getCreatedTime().before(endTime) && bankCardModel.getCreatedTime().after(startTime)){
            lotteryTime ++;
        }

        if(rechargeMapper.findRechargeCount(null, userModel.getMobile(), null, RechargeStatus.SUCCESS, null, startTime, endTime) > 0){
            lotteryTime ++;
        }

        if(investMapper.countInvestorSuccessInvestByInvestTime(userModel.getLoginName(), startTime, endTime) > 0){
            lotteryTime ++;
        }

        return lotteryTime;
    }


    private int findLotteryTimeByChristmas(UserModel userModel, Date startTime, Date endTime){
        int lotteryTime = 0;

        List<UserModel> userModels = userMapper.findUsersByRegisterTimeOrReferrer(startTime,endTime, userModel.getLoginName());
        for(UserModel referrerUserModel : userModels){
            if(referrerUserModel.getRegisterTime().before(endTime) && referrerUserModel.getRegisterTime().after(startTime)){
                lotteryTime ++;
                if(investMapper.countInvestorSuccessInvestByInvestTime(referrerUserModel.getLoginName(),startTime,endTime) > 0){
                    lotteryTime ++;
                }
            }
        }

        if(userModel.getRegisterTime().before(endTime) && userModel.getRegisterTime().after(startTime)){
            lotteryTime ++;
        }

        AccountModel accountModel = accountMapper.findByLoginName(userModel.getLoginName());
        if(accountModel != null && accountModel.getRegisterTime().before(endTime) && accountModel.getRegisterTime().after(startTime)){
            lotteryTime ++;
        }

        if(investMapper.sumInvestAmountByLoginNameInvestTimeProductType(userModel.getLoginName(), startTime, endTime, null) > 0){
            lotteryTime++;
        }

        long sumAmount = investMapper.sumInvestAmountByLoginNameInvestTimeProductType(userModel.getLoginName(), startTime, endTime, Lists.newArrayList(ProductType._90, ProductType._180, ProductType._360));
        lotteryTime += (int)(sumAmount/200000);

        lotteryTime = lotteryTime >= 10 ? 10 : lotteryTime;

        return lotteryTime;
    }
}
