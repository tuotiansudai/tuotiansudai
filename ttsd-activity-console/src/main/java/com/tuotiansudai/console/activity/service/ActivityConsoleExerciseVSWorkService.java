package com.tuotiansudai.console.activity.service;

import com.tuotiansudai.activity.repository.mapper.UserExchangePrizeMapper;
import com.tuotiansudai.activity.repository.model.ActivityCategory;
import com.tuotiansudai.activity.repository.model.ExchangePrize;
import com.tuotiansudai.activity.repository.model.UserExchangePrizeModel;
import com.tuotiansudai.activity.repository.model.UserExchangePrizeView;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.repository.mapper.InvestMapper;
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

    @Autowired
    private InvestMapper investMapper;

    public BasePaginationDataDto<UserExchangePrizeModel> exchangePrizeList(int index, int pageSize){

        List<UserExchangePrizeModel> userExchangePrizeModels = userExchangePrizeMapper.findUserExchangePrizeViews(null,null, ActivityCategory.EXERCISE_WORK_ACTIVITY, startTime, endTime);

        List<UserExchangePrizeView> list=new ArrayList<>();
        for (UserExchangePrizeModel userExchangePrizeModel:userExchangePrizeModels) {
            list.add(new UserExchangePrizeView(userExchangePrizeModel,AmountConverter.convertCentToString(investMapper.findSuccessByLoginNameExceptTransferAndTime(userExchangePrizeModel.getLoginName(),startTime,endTime).stream().mapToLong(i->i.getAmount()).sum())));
        }

        int count=list.size();
        int endIndex = pageSize * index;
        int startIndex = (index - 1) * 10;
        if (count <= endIndex) {
            endIndex = count;
        }
        if (count < startIndex) {
            startIndex = count;
        }
        BasePaginationDataDto basePaginationDataDto = new BasePaginationDataDto(index, pageSize, count, list.subList(startIndex, endIndex));
        return basePaginationDataDto;

    }



}
