package com.tuotiansudai.console.activity.service;


import com.tuotiansudai.activity.repository.model.ActivityCategory;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CountNewYearsDrawTimeService extends CommonCountTimeService{

    @Autowired
    private UserMapper userMapper;

    @Override
    public int countDrawLotteryTime(String mobile, ActivityCategory activityCategory) {
        //默认登录就有一次抽奖机会
        int drawLotteryTime = 1;
        UserModel userModel = userMapper.findByMobile(mobile);

        if (userModel == null) return drawLotteryTime;

        Date startTime = getActivityStartTime(activityCategory);
        Date endTime = getActivityEndTime(activityCategory);

        List<UserModel> userModels = userMapper.findUsersByRegisterTimeOrReferrer(startTime, endTime, userModel.getLoginName());

        drawLotteryTime += userModels.size() * 5;




        return drawLotteryTime;
    }
}
