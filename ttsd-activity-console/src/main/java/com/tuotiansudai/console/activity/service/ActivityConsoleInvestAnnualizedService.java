package com.tuotiansudai.console.activity.service;

import com.tuotiansudai.activity.repository.mapper.ActivityInvestAnnualizedMapper;
import com.tuotiansudai.activity.repository.model.ActivityInvestAnnualized;
import com.tuotiansudai.activity.repository.model.ActivityInvestAnnualizedView;
import com.tuotiansudai.dto.BasePaginationDataDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityConsoleInvestAnnualizedService {

    @Autowired
    private ActivityInvestAnnualizedMapper activityInvestAnnualizedMapper;

    public BasePaginationDataDto<ActivityInvestAnnualizedView> list(ActivityInvestAnnualized activityInvestAnnualized, String mobile, int index, int pageSize) {
        List<ActivityInvestAnnualizedView> activityInvestAnnualizedViews = activityInvestAnnualizedMapper.findByActivityAndMobile(activityInvestAnnualized, mobile);
        int count = activityInvestAnnualizedViews.size();
        int endIndex = pageSize * index;
        int startIndex = (index - 1) * pageSize;
        if (count <= endIndex) {
            endIndex = count;
        }
        if (count < startIndex) {
            startIndex = count;
        }
        BasePaginationDataDto basePaginationDataDto = new BasePaginationDataDto(index, pageSize, count, activityInvestAnnualizedViews.subList(startIndex, endIndex));
        return basePaginationDataDto;
    }
}
