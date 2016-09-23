package com.tuotiansudai.console.activity.service;


import com.google.common.base.Function;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.tuotiansudai.activity.dto.ActivityCategory;
import com.tuotiansudai.activity.dto.LotteryPrize;
import com.tuotiansudai.activity.repository.mapper.UserLotteryPrizeMapper;
import com.tuotiansudai.activity.repository.model.UserLotteryPrizeView;
import com.tuotiansudai.activity.repository.model.UserLotteryTimeView;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.BankCardModel;
import com.tuotiansudai.repository.model.RechargeStatus;
import com.tuotiansudai.repository.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Service
public class UserLotteryService{

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

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.autumn.startTime}\")}")
    private Date activityAutumnStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.autumn.endTime}\")}")
    private Date activityAutumnEndTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.national.startTime}\")}")
    private Date activityNationalStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.national.endTime}\")}")
    private Date activityNationalEndTime;

    public List<UserLotteryTimeView> findUserLotteryTimeViews(String mobile,final ActivityCategory prizeType,Integer index,Integer pageSize) {
        List<UserModel> userModels = userMapper.findUserModelByMobile(mobile,index, pageSize);

        Iterator<UserLotteryTimeView> transform = Iterators.transform(userModels.iterator(), new Function<UserModel, UserLotteryTimeView>() {
            @Override
            public UserLotteryTimeView apply(UserModel input) {
                UserLotteryTimeView model = new UserLotteryTimeView(input.getMobile(),input.getLoginName());
                model.setUseCount(userLotteryPrizeMapper.findUserLotteryPrizeCountViews(input.getMobile(), null,prizeType, null, null));
                model.setUnUseCount((findLotteryTime(model.getMobile(),prizeType) - model.getUseCount()));
                return model;
            }
        });

        return Lists.newArrayList(transform);
    }

    public int findUserLotteryTimeCountViews(String mobile){
        return userMapper.findUserModelByMobile(mobile,null, null).size();
    }

    public List<UserLotteryPrizeView> findUserLotteryPrizeViews(String mobile,LotteryPrize selectPrize,ActivityCategory prizeType,Date startTime,Date endTime,Integer index,Integer pageSize){
        return userLotteryPrizeMapper.findUserLotteryPrizeViews(mobile, selectPrize,prizeType, startTime, endTime, index, pageSize);
    }

    public int findUserLotteryPrizeCountViews(String mobile,LotteryPrize selectPrize,ActivityCategory prizeType,Date startTime,Date endTime){
        return userLotteryPrizeMapper.findUserLotteryPrizeCountViews(mobile, selectPrize, prizeType,startTime, endTime);
    }

    private int findLotteryTime(String mobile,ActivityCategory activityCategory){
        int lotteryTime = 0;
        UserModel userModel = userMapper.findByMobile(mobile);
        if(userModel == null){
            return lotteryTime;
        }
        Date startTime = activityCategory.equals(ActivityCategory.AUTUMN_PRIZE) ? activityAutumnStartTime : activityNationalStartTime;
        Date endTime = activityCategory.equals(ActivityCategory.AUTUMN_PRIZE) ? activityAutumnEndTime : activityNationalEndTime;
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

}
