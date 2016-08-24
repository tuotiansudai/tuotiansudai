package com.tuotiansudai.console.activity.service.impl;


import com.google.common.base.Function;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.tuotiansudai.activity.dto.LotteryPrize;
import com.tuotiansudai.activity.repository.mapper.UserLotteryPrizeMapper;
import com.tuotiansudai.activity.repository.mapper.UserLotteryTimeMapper;
import com.tuotiansudai.activity.repository.model.UserLotteryPrizeModel;
import com.tuotiansudai.activity.repository.model.UserLotteryPrizeView;
import com.tuotiansudai.activity.repository.model.UserLotteryTimeView;
import com.tuotiansudai.console.activity.service.UserLotteryService;
import com.tuotiansudai.repository.mapper.UserMapper;
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

    @Override
    public List<UserLotteryTimeView> findUserLotteryTimeViews(final String mobile,Integer index,Integer pageSize) {
        List<UserModel> userModels = userMapper.findUserModelByMobile(mobile,index, pageSize);

        Iterator<UserLotteryTimeView> transform = Iterators.transform(userModels.iterator(), new Function<UserModel, UserLotteryTimeView>() {
            @Override
            public UserLotteryTimeView apply(UserModel input) {
                UserLotteryTimeView model = new UserLotteryTimeView(input.getMobile(),input.getLoginName());
                model.setUseCount(userLotteryPrizeMapper.findUserLotteryPrizeCountViews(mobile,null,null,null));
                model.setUnUseCount(0);
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

}
