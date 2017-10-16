package com.tuotiansudai.activity.service;


import com.google.common.base.Strings;
import com.tuotiansudai.activity.repository.mapper.UserLotteryPrizeMapper;
import com.tuotiansudai.activity.repository.model.ActivityCategory;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.model.InvestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class IphoneXService {

    @Autowired
    private UserLotteryPrizeMapper userLotteryPrizeMapper;

    @Autowired
    private InvestMapper investMapper;

    private final long EACH_INVEST_AMOUNT_100000 = 1000000L;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.iphoneX.startTime}\")}")
    private Date activityIphoneXStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.iphoneX.endTime}\")}")
    private Date activityIphoneXEndTime;

    public int iphoneXDrawTime(String mobile,String loginName){

        if(Strings.isNullOrEmpty(loginName)){
            return 0;
        }
        int time=0;
        List<InvestModel> investModels=investMapper.findSuccessByLoginNameExceptTransferAndTime(loginName,activityIphoneXStartTime,activityIphoneXEndTime);
        for (InvestModel investModel:investModels) {
            time+=investModel.getAmount()<EACH_INVEST_AMOUNT_100000?0:(int)(investModel.getAmount()/EACH_INVEST_AMOUNT_100000);
        }
        return time > 0 ? time - userLotteryPrizeMapper.findUserLotteryPrizeCountViews(mobile, null, ActivityCategory.IPHONEX_ACTIVITY, activityIphoneXStartTime, activityIphoneXEndTime) : time;

    }
}
