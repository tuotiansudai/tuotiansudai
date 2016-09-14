package com.tuotiansudai.console.activity.service;


import com.google.common.base.Function;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.tuotiansudai.activity.dto.LotteryPrize;
import com.tuotiansudai.activity.dto.PrizeType;
import com.tuotiansudai.activity.repository.mapper.UserLotteryPrizeMapper;
import com.tuotiansudai.activity.repository.model.UserLotteryPrizeView;
import com.tuotiansudai.activity.repository.model.UserLotteryTimeView;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import org.joda.time.DateTime;
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
    private ReferrerRelationMapper referrerRelationMapper;

    @Autowired
    private RechargeMapper rechargeMapper;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.autumn.startTime}\")}")
    private Date activityAutumnStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.autumn.endTime}\")}")
    private Date activityAutumnEndTime;

    public List<UserLotteryTimeView> findUserLotteryTimeViews(String mobile,final PrizeType prizeType,Integer index,Integer pageSize) {
        List<UserModel> userModels = userMapper.findUserModelByMobile(mobile,index, pageSize);

        Iterator<UserLotteryTimeView> transform = Iterators.transform(userModels.iterator(), new Function<UserModel, UserLotteryTimeView>() {
            @Override
            public UserLotteryTimeView apply(UserModel input) {
                UserLotteryTimeView model = new UserLotteryTimeView(input.getMobile(),input.getLoginName());
                model.setUseCount(userLotteryPrizeMapper.findUserLotteryPrizeCountViews(input.getMobile(), null,prizeType, null, null));
                model.setUnUseCount((findLotteryTime(model.getMobile()) - model.getUseCount()));
                return model;
            }
        });

        return Lists.newArrayList(transform);
    }

    public int findUserLotteryTimeCountViews(String mobile){
        return userMapper.findUserModelByMobile(mobile,0, Integer.MAX_VALUE).size();
    }

    public List<UserLotteryPrizeView> findUserLotteryPrizeViews(String mobile,LotteryPrize selectPrize,PrizeType prizeType,Date startTime,Date endTime,Integer index,Integer pageSize){
        return userLotteryPrizeMapper.findUserLotteryPrizeViews(mobile, selectPrize,prizeType, startTime, endTime, index, pageSize);
    }

    public int findUserLotteryPrizeCountViews(String mobile,LotteryPrize selectPrize,PrizeType prizeType,Date startTime,Date endTime){
        return userLotteryPrizeMapper.findUserLotteryPrizeCountViews(mobile, selectPrize, prizeType,startTime, endTime);
    }

    private int findLotteryTime(String mobile){
        int lotteryTime = 0;
        UserModel userModel = userMapper.findByMobile(mobile);
        if(userModel == null){
            return lotteryTime;
        }
        List<ReferrerRelationModel> referrerRelationModels = referrerRelationMapper.findByReferrerLoginNameAndLevel(userModel.getLoginName(), 1);
        for(ReferrerRelationModel referrerRelationModel : referrerRelationModels){
            UserModel referrerUserModel = userMapper.findByLoginName(referrerRelationModel.getLoginName());
            if(referrerUserModel.getRegisterTime().before(activityAutumnEndTime) && referrerUserModel.getRegisterTime().after(activityAutumnStartTime)){
                lotteryTime ++;
                if(investMapper.countInvestorSuccessInvestByInvestTime(referrerUserModel.getLoginName(),activityAutumnStartTime,activityAutumnEndTime) > 0){
                    lotteryTime ++;
                }
            }
        }

        if(userModel.getRegisterTime().before(activityAutumnEndTime) && userModel.getRegisterTime().after(activityAutumnStartTime)){
            lotteryTime ++;
        }

        AccountModel accountModel = accountMapper.findByLoginName(userModel.getLoginName());
        if(accountModel != null && accountModel.getRegisterTime().before(activityAutumnEndTime) && accountModel.getRegisterTime().after(activityAutumnStartTime)){
            lotteryTime ++;
        }

        BankCardModel bankCardModel = bankCardMapper.findPassedBankCardByLoginName(userModel.getLoginName());
        if(bankCardModel != null && bankCardModel.getCreatedTime().before(activityAutumnEndTime) && bankCardModel.getCreatedTime().after(activityAutumnStartTime)){
            lotteryTime ++;
        }

        if(rechargeMapper.findRechargeCount(null, userModel.getMobile(), null, RechargeStatus.SUCCESS, null, activityAutumnStartTime, activityAutumnEndTime) > 0){
            lotteryTime ++;
        }

        if(investMapper.countInvestorSuccessInvestByInvestTime(userModel.getLoginName(), activityAutumnStartTime, activityAutumnEndTime) > 0){
            lotteryTime ++;
        }

        return lotteryTime;
    }

}
