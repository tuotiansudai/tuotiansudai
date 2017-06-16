package com.tuotiansudai.activity.service;

import com.google.common.base.Strings;
import com.tuotiansudai.activity.repository.mapper.UserLotteryPrizeMapper;
import com.tuotiansudai.activity.repository.model.ActivityCategory;
import com.tuotiansudai.activity.repository.model.UserLotteryTimeView;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.model.InvestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CelebrationSingleActivityService {

    @Autowired
    private InvestMapper investMapper;
    @Autowired
    private UserLotteryPrizeMapper userLotteryPrizeMapper;

    private final long EACH_INVEST_AMOUNT_100000 = 1000000L;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.celebration.single.startTime}\")}")
    private Date activitySingleStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.celebration.single.endTime}\")}")
    private Date activitySingleEndTime;

    public int drawTimeByLoginNameAndActivityCategory(String mobile,String loginName){

        if(Strings.isNullOrEmpty(loginName)){
            return 0;
        }
        int time=0;
        List<InvestModel> investModels=investMapper.findSuccessByLoginNameExceptTransferAndTime(loginName,activitySingleStartTime,activitySingleEndTime);
        for (InvestModel investModel:investModels) {
            time+=investModel.getAmount()<EACH_INVEST_AMOUNT_100000?0:(int)(investModel.getAmount()/EACH_INVEST_AMOUNT_100000);
        }
        return time > 0 ? time - userLotteryPrizeMapper.findUserLotteryPrizeCountViews(mobile, null, ActivityCategory.CELEBRATION_SINGLE_ACTIVITY, activitySingleStartTime, activitySingleEndTime) : time;

    }

}
