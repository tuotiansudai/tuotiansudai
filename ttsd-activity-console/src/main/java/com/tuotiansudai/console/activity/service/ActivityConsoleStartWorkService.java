package com.tuotiansudai.console.activity.service;

import com.tuotiansudai.activity.repository.dto.StartWorkPrizeDto;
import com.tuotiansudai.activity.repository.mapper.UserExchangePrizeMapper;
import com.tuotiansudai.activity.repository.model.ActivityCategory;
import com.tuotiansudai.activity.repository.model.UserExchangePrizeModel;
import com.tuotiansudai.dto.BasePaginationDataDto;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ActivityConsoleStartWorkService {

    @Autowired
    private UserExchangePrizeMapper userExchangePrizeMapper;

    public BasePaginationDataDto<StartWorkPrizeDto> list(String mobile, String userName, Date startTime, Date endTime, int index, int pageSize){
        if (startTime != null) {
            startTime = new DateTime(startTime).withTimeAtStartOfDay().toDate();
        }
        if (endTime != null) {
            endTime = new DateTime(endTime).withTimeAtStartOfDay().plusDays(1).minusMillis(1).toDate();
        }
        List<UserExchangePrizeModel> userExchangePrizeModels = userExchangePrizeMapper.findUserExchangePrizeViews(mobile, userName, null, ActivityCategory.START_WORK_ACTIVITY, startTime, endTime, null, null);
        List<StartWorkPrizeDto> list = userExchangePrizeModels.stream().map(StartWorkPrizeDto::new).collect(Collectors.toList());
        int count=list.size();
        int endIndex = pageSize * index;
        int startIndex = (index - 1) * pageSize;
        if (count <= endIndex) {
            endIndex = count;
        }
        if (count < startIndex) {
            startIndex = count;
        }
        return new BasePaginationDataDto(index, pageSize, count, list.subList(startIndex, endIndex));
    }
}
