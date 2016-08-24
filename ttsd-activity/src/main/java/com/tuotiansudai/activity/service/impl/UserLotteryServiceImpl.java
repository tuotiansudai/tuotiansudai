package com.tuotiansudai.activity.service.impl;


import com.google.common.base.Strings;
import com.tuotiansudai.activity.dto.LotteryPrize;
import com.tuotiansudai.activity.dto.LotteryTaskStatus;
import com.tuotiansudai.activity.dto.UserLotteryDto;
import com.tuotiansudai.activity.repository.mapper.UserLotteryPrizeMapper;
import com.tuotiansudai.activity.repository.mapper.UserLotteryTimeMapper;
import com.tuotiansudai.activity.repository.model.UserLotteryPrizeView;
import com.tuotiansudai.activity.service.UserLotteryService;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.BankCardModel;
import com.tuotiansudai.repository.model.ReferrerRelationModel;
import com.tuotiansudai.repository.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
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
    public List<UserLotteryPrizeView> findUserLotteryPrizeViews(String mobile,LotteryPrize selectPrize,Date startTime,Date endTime,Integer index,Integer pageSize){
        return userLotteryPrizeMapper.findUserLotteryPrizeViews(mobile, selectPrize, startTime, endTime, index, pageSize);
    }

    @Override
    public int findUserLotteryPrizeCountViews(String mobile,LotteryPrize selectPrize,Date startTime,Date endTime){
        return userLotteryPrizeMapper.findUserLotteryPrizeCountViews(mobile, selectPrize, startTime, endTime);
    }

    @Override
    public UserLotteryDto findUserLotteryByLoginName(String mobile){
        int lotteryTime = 0;
        UserLotteryDto userLotteryDto = new UserLotteryDto(lotteryTime,LotteryTaskStatus.UNFINISHED);

        if (Strings.isNullOrEmpty(mobile)) {
            return userLotteryDto;
        }

        UserModel userModel = userMapper.findByMobile(mobile);
        if(userModel == null){
            return userLotteryDto;
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
            userLotteryDto.setRegisterStatus(LotteryTaskStatus.COMPLETE);
        }else{
            userLotteryDto.setRegisterStatus(LotteryTaskStatus.PROCEED);
            return userLotteryDto;
        }

        AccountModel accountModel = accountMapper.findByLoginName(userModel.getLoginName());
        if(accountModel.getRegisterTime().before(endTime) && accountModel.getRegisterTime().after(startTime)){
            lotteryTime ++;
            userLotteryDto.setCertificationStatus(LotteryTaskStatus.COMPLETE);
        }else{
            userLotteryDto.setCertificationStatus(LotteryTaskStatus.PROCEED);
            return userLotteryDto;
        }

        BankCardModel bankCardModel = bankCardMapper.findPassedBankCardByLoginName(userModel.getLoginName());
        if(bankCardModel.getCreatedTime().before(endTime) && bankCardModel.getCreatedTime().after(startTime)){
            lotteryTime ++;
            userLotteryDto.setBindCardStatus(LotteryTaskStatus.COMPLETE);
        }else{
            userLotteryDto.setBindCardStatus(LotteryTaskStatus.PROCEED);
            return userLotteryDto;
        }

        if(investMapper.countInvestorInvestPagination(userModel.getLoginName(),null,startTime,endTime) > 0){
            lotteryTime ++;
            userLotteryDto.setInvestStatus(LotteryTaskStatus.COMPLETE);
        }else{
            userLotteryDto.setInvestStatus(LotteryTaskStatus.PROCEED);
            return userLotteryDto;
        }
        return userLotteryDto;
    }
}
