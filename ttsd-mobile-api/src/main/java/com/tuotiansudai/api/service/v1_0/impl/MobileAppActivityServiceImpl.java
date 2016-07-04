package com.tuotiansudai.api.service.v1_0.impl;

import com.tuotiansudai.api.dto.v1_0.ActivityCenterDataDto;
import com.tuotiansudai.api.dto.v1_0.ActivityCenterResponseDto;
import com.tuotiansudai.api.service.v1_0.MobileAppActivityService;
import com.tuotiansudai.repository.mapper.ActivityMapper;
import com.tuotiansudai.repository.model.ActivityModel;
import com.tuotiansudai.repository.model.Source;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MobileAppActivityServiceImpl implements MobileAppActivityService {

    @Autowired
    ActivityMapper activityMapper;

    @Override
    public ActivityCenterResponseDto getAppActivityCenterResponseData(String loginName, Source source, Integer index, Integer pageSize) {
        if (null == index) {
            index = 1;
        }
        if (null == pageSize) {
            pageSize = 10;
        }

        List<ActivityModel> activityModels = activityMapper.findActiveActivities(source, (index - 1) * pageSize, pageSize);

        List<ActivityCenterDataDto> activityCenterDataDtos = new ArrayList<>();
        for (ActivityModel activityModel : activityModels) {
            activityCenterDataDtos.add(new ActivityCenterDataDto(activityModel));
        }

        ActivityCenterResponseDto activityCenterResponseDto = new ActivityCenterResponseDto();
        activityCenterResponseDto.setIndex(index);
        activityCenterResponseDto.setPageSize(pageSize);
        activityCenterResponseDto.setTotalCount(activityMapper.countActiveActivities(source));
        activityCenterResponseDto.setActivities(activityCenterDataDtos);

        return activityCenterResponseDto;
    }
}
