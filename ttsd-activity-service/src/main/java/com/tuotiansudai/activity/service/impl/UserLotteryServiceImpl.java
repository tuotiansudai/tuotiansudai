package com.tuotiansudai.activity.service.impl;


import com.tuotiansudai.activity.dto.UserLotteryDto;
import com.tuotiansudai.activity.repository.mapper.UserLotteryPrizeMapper;
import com.tuotiansudai.activity.repository.mapper.UserLotteryTimeMapper;
import com.tuotiansudai.activity.repository.model.LotteryPrize;
import com.tuotiansudai.activity.repository.model.LotteryTaskStatus;
import com.tuotiansudai.activity.repository.model.UserLotteryPrizeView;
import com.tuotiansudai.activity.repository.model.UserLotteryTimeView;
import com.tuotiansudai.activity.service.UserLotteryService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
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

    @Override
    public List<UserLotteryTimeView> findUserLotteryTimeViews(String mobile,Integer index,Integer pageSize) {
        return userLotteryTimeMapper.findUserLotteryTimeViews(mobile, index, pageSize);
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

    @Override
    public UserLotteryDto findUserLotteryByLoginName(String mobile){
        UserLotteryDto userLotteryDto = new UserLotteryDto();
        int time = 0;
        UserModel userModel = userMapper.findByMobile(mobile);
        if(userMapper.findByMobile(mobile) != null){
            userLotteryDto.setRegisterStatus(LotteryTaskStatus.COMPLETE);
        }

        if(accountMapper.findByLoginName(userModel.getLoginName()) != null){
            userLotteryDto.setCertificationStatus(LotteryTaskStatus.COMPLETE);
        }




        userLotteryDto.setLotteryTime(String.valueOf(time));
        return userLotteryDto;
    }
}
