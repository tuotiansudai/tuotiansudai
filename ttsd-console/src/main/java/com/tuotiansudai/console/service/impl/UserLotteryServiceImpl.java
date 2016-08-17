package com.tuotiansudai.console.service.impl;


import com.tuotiansudai.console.service.UserLotteryService;
import com.tuotiansudai.repository.mapper.UserLotteryTimeMapper;
import com.tuotiansudai.repository.model.UserLotteryTimeModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserLotteryServiceImpl implements UserLotteryService {

    @Autowired
    private UserLotteryTimeMapper userLotteryTimeMapper;

    @Override
    public List<UserLotteryTimeModel> findUserLotteryTimeModels(String loginName,Integer index,Integer pageSize) {
        return userLotteryTimeMapper.findUserLotteryTimeModels(loginName,index,pageSize);
    }

    @Override
    public int findUserLotteryTimeCountModels(String loginName){
        return userLotteryTimeMapper.findUserLotteryTimeCountModels(loginName);
    }
}
