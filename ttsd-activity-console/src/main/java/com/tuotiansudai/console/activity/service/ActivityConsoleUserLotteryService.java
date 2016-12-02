package com.tuotiansudai.console.activity.service;


import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.tuotiansudai.activity.repository.mapper.UserLotteryPrizeMapper;
import com.tuotiansudai.activity.repository.model.ActivityCategory;
import com.tuotiansudai.activity.repository.model.LotteryPrize;
import com.tuotiansudai.activity.repository.model.UserLotteryPrizeView;
import com.tuotiansudai.activity.repository.model.UserLotteryTimeView;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
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
    private ActivityCountDrawLotteryService commonCountTimeService;

    public List<UserLotteryTimeView> findUserLotteryTimeViews(String mobile, final ActivityCategory prizeType, Integer index, Integer pageSize) {
        List<UserModel> userModels = userMapper.findUserModelByMobile(mobile, index, pageSize);

        Iterator<UserLotteryTimeView> transform = Iterators.transform(userModels.iterator(), input -> {
            UserLotteryTimeView model = new UserLotteryTimeView(input.getMobile(), input.getLoginName());
            model.setUseCount(userLotteryPrizeMapper.findUserLotteryPrizeCountViews(input.getMobile(), null, prizeType, null, null));
            model.setUnUseCount((commonCountTimeService.countDrawLotteryTime(model.getMobile(), prizeType) - model.getUseCount()));
            return model;
        });

        return Lists.newArrayList(transform);
    }

    public int findUserLotteryTimeCountViews(String mobile) {
        return userMapper.findUserModelByMobile(mobile, null, null).size();
    }

    public List<UserLotteryPrizeView> findUserLotteryPrizeViews(String mobile, LotteryPrize selectPrize, ActivityCategory prizeType, Date startTime, Date endTime, Integer index, Integer pageSize) {
        return userLotteryPrizeMapper.findUserLotteryPrizeViews(mobile, selectPrize, prizeType, startTime, endTime, index, pageSize);
    }

    public int findUserLotteryPrizeCountViews(String mobile, LotteryPrize selectPrize, ActivityCategory prizeType, Date startTime, Date endTime) {
        return userLotteryPrizeMapper.findUserLotteryPrizeCountViews(mobile, selectPrize, prizeType, startTime, endTime);
    }
}
