package com.tuotiansudai.console.activity.service.impl;


import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.tuotiansudai.activity.dto.LotteryPrize;
import com.tuotiansudai.activity.repository.mapper.UserLotteryPrizeMapper;
import com.tuotiansudai.activity.repository.mapper.UserLotteryTimeMapper;
import com.tuotiansudai.activity.repository.model.UserLotteryPrizeModel;
import com.tuotiansudai.activity.repository.model.UserLotteryPrizeView;
import com.tuotiansudai.activity.repository.model.UserLotteryTimeView;
import com.tuotiansudai.console.activity.service.UserLotteryService;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.BankCardModel;
import com.tuotiansudai.repository.model.ReferrerRelationModel;
import com.tuotiansudai.repository.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Service
public class UserLotteryServiceImpl implements UserLotteryService {

    @Autowired
    private UserLotteryPrizeMapper userLotteryPrizeMapper;

    @Autowired
    private UserLotteryTimeMapper userLotteryTimeMapper;

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

    private Date startTime;

    private Date endTime;

    @Override
    public List<UserLotteryTimeView> findUserLotteryTimeViews(final String mobile,Integer index,Integer pageSize) {
        List<UserModel> userModels = userMapper.findUserModelByMobile(mobile,index, pageSize);

        Iterator<UserLotteryTimeView> transform = Iterators.transform(userModels.iterator(), new Function<UserModel, UserLotteryTimeView>() {
            @Override
            public UserLotteryTimeView apply(UserModel input) {
                UserLotteryTimeView model = new UserLotteryTimeView(input.getMobile(),input.getLoginName());
                model.setUseCount(userLotteryPrizeMapper.findUserLotteryPrizeCountViews(mobile,null,null,null));
                model.setUnUseCount(findLotteryTime(model.getMobile()));
                return model;
            }
        });

        return Lists.newArrayList(transform);
    }

    @Override
    public int findUserLotteryTimeCountViews(String loginName){
        return userLotteryTimeMapper.findUserLotteryTimeCountModels(loginName);
    }

    @Override
    public List<UserLotteryPrizeView> findUserLotteryPrizeViews(String mobile,LotteryPrize selectPrize,Date startTime,Date endTime,Integer index,Integer pageSize){
        return userLotteryPrizeMapper.findUserLotteryPrizeViews(mobile, selectPrize, startTime, endTime, index, pageSize);
    }

    @Override
    public int findUserLotteryPrizeCountViews(String mobile,LotteryPrize selectPrize,Date startTime,Date endTime){
        return userLotteryPrizeMapper.findUserLotteryPrizeCountViews(mobile, selectPrize, startTime, endTime);
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
            if(referrerUserModel.getRegisterTime().before(endTime) && referrerUserModel.getRegisterTime().after(startTime)){
                lotteryTime ++;
                if(investMapper.countInvestorInvestPagination(referrerUserModel.getLoginName(),null,startTime,endTime) > 0){
                    lotteryTime ++;
                }
            }
        }

        if(userModel.getRegisterTime().before(endTime) && userModel.getRegisterTime().after(startTime)){
            lotteryTime ++;
        }

        AccountModel accountModel = accountMapper.findByLoginName(userModel.getLoginName());
        if(accountModel.getRegisterTime().before(endTime) && accountModel.getRegisterTime().after(startTime)){
            lotteryTime ++;
        }

        BankCardModel bankCardModel = bankCardMapper.findPassedBankCardByLoginName(userModel.getLoginName());
        if(bankCardModel.getCreatedTime().before(endTime) && bankCardModel.getCreatedTime().after(startTime)){
            lotteryTime ++;
        }

        if(investMapper.countInvestorInvestPagination(userModel.getLoginName(), null, startTime, endTime) > 0){
            lotteryTime ++;
        }

        return lotteryTime;
    }

}
