package com.tuotiansudai.activity.service;

import com.google.common.base.Strings;
import com.tuotiansudai.activity.repository.mapper.UserLotteryPrizeMapper;
import com.tuotiansudai.activity.repository.model.ActivityCategory;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.InvestStatus;
import com.tuotiansudai.util.AmountConverter;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;
import java.util.List;

public class ExerciseVSWorkActivityService {

    private final long EACH_INVEST_AMOUNT_100000 = 1000000L;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.exercise.work.startTime}\")}")
    private Date startTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.exercise.work.endTime}\")}")
    private Date endTime;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private UserLotteryPrizeMapper userLotteryPrizeMapper;

    public int drawTimeByLoginNameAndActivityCategory(String mobile,String loginName){
        if(Strings.isNullOrEmpty(loginName)){
            return 0;
        }
        int time=0;

        time = userLotteryPrizeMapper.findUserLotteryPrizeCountViews(mobile, null,ActivityCategory.EXERCISE_WORK_ACTIVITY,
                DateTime.now().withTimeAtStartOfDay().toDate(), DateTime.now().plusDays(1).withTimeAtStartOfDay().plusMillis(-1).toDate()) == 0 ? 1 : 0;

        List<InvestModel> investModels=investMapper.findSuccessByLoginNameExceptTransferAndTime(loginName,startTime,endTime);
        for (InvestModel investModel:investModels) {
            time+=investModel.getAmount()<EACH_INVEST_AMOUNT_100000?0:(int)(investModel.getAmount()/EACH_INVEST_AMOUNT_100000);
        }
        return time > 0 ? time - userLotteryPrizeMapper.findUserLotteryPrizeCountViews(mobile, null, ActivityCategory.EXERCISE_WORK_ACTIVITY,startTime,endTime) : time;

    }

    public String sumInvestByLoginNameExceptTransferAndTime(String loginName){
        if(Strings.isNullOrEmpty(loginName)){
            return "0";
        }

        List<InvestModel> investModels=investMapper.findSuccessByLoginNameExceptTransferAndTime(loginName,startTime,endTime);
        long amount=0;
        for (InvestModel investModel:investModels) {
            amount+=investModel.getAmount();
        }
        return AmountConverter.convertCentToString(amount);
    }


}
