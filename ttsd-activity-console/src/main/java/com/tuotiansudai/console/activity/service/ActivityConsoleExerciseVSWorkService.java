package com.tuotiansudai.console.activity.service;

import com.tuotiansudai.activity.repository.mapper.UserExchangePrizeMapper;
import com.tuotiansudai.activity.repository.model.ActivityCategory;
import com.tuotiansudai.activity.repository.model.ExchangePrize;
import com.tuotiansudai.activity.repository.model.UserExchangePrizeModel;
import com.tuotiansudai.activity.repository.model.UserExchangePrizeView;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.util.AmountConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ActivityConsoleExerciseVSWorkService {

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.exercise.work.startTime}\")}")
    private Date startTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.exercise.work.endTime}\")}")
    private Date endTime;


    @Autowired
    private UserExchangePrizeMapper userExchangePrizeMapper;

    public BasePaginationDataDto<UserExchangePrizeModel> exchangePrizeList(int index, int pageSize){

        List<UserExchangePrizeModel> list=userExchangePrizeMapper.findUserExchangePrizeViews(null,null,ActivityCategory.EXERCISE_WORK_ACTIVITY,startTime,endTime,null,null);
        List<UserExchangePrizeView> userExchangePrizeViews=new ArrayList<>();

        for (UserExchangePrizeModel userExchangePrizeModel:list) {
            UserExchangePrizeView userExchangePrizeView=new UserExchangePrizeView();
            userExchangePrizeView.setMobile(userExchangePrizeModel.getMobile());
            userExchangePrizeView.setInvestAmount(AmountConverter.convertCentToString(userExchangePrizeModel.getInvestAmount()));
            userExchangePrizeView.setLoginName(userExchangePrizeModel.getLoginName());
            userExchangePrizeView.setUserName(userExchangePrizeModel.getUserName());
            userExchangePrizeView.setPrize(userExchangePrizeModel.getPrize().getPrizeName());
            userExchangePrizeViews.add(userExchangePrizeView);
        }
        int count=userExchangePrizeViews.size();
        int endIndex = pageSize * index;
        int startIndex = (index - 1) * 10;
        if (count <= endIndex) {
            endIndex = count;
        }
        if (count < startIndex) {
            startIndex = count;
        }
        BasePaginationDataDto basePaginationDataDto = new BasePaginationDataDto(index, pageSize, count, userExchangePrizeViews.subList(startIndex, endIndex));
        return basePaginationDataDto;

    }



}
