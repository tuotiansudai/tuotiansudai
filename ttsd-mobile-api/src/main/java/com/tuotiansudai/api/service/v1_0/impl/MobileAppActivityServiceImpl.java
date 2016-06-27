package com.tuotiansudai.api.service.v1_0.impl;

import com.tuotiansudai.api.dto.v1_0.ActivityCenterDataDto;
import com.tuotiansudai.api.dto.v1_0.ActivityCenterResponseDto;
import com.tuotiansudai.api.service.v1_0.MobileAppActivityService;
import com.tuotiansudai.repository.mapper.ActivityMapper;
import com.tuotiansudai.repository.model.ActivityModel;
import com.tuotiansudai.repository.model.ActivityStatus;
import com.tuotiansudai.repository.model.Source;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class MobileAppActivityServiceImpl implements MobileAppActivityService {

    @Autowired
    ActivityMapper activityMapper;

    @Override
    public ActivityCenterResponseDto getAppActivityCenterResponseData(String loginName, Integer index, Integer pageSize) {
        List<ActivityModel> activityModels = activityMapper.findActivities(null, null, ActivityStatus.OPERATING, Source.ANDROID);
        activityModels.addAll(activityMapper.findActivities(null, null, ActivityStatus.OPERATING, Source.IOS));

        List<ActivityCenterDataDto> activityCenterDataDtos = new ArrayList<>();
        for (ActivityModel activityModel : activityModels) {
            activityCenterDataDtos.add(new ActivityCenterDataDto(activityModel));
        }

        ActivityCenterResponseDto activityCenterResponseDto = new ActivityCenterResponseDto();
        activityCenterResponseDto.setIndex(index);
        activityCenterResponseDto.setPageSize(pageSize);
        activityCenterResponseDto.setTotalCount(activityCenterDataDtos.size());
        if (null == index || null == pageSize) {
            activityCenterResponseDto.setActivities(activityCenterDataDtos);
        } else {
            List<ActivityCenterDataDto> results = new ArrayList<>();
            for (int startIndex = (index - 1) * pageSize,
                 endIndex = index * pageSize <= activityCenterDataDtos.size() ? index * pageSize : activityCenterDataDtos.size();
                 startIndex < endIndex; ++startIndex) {
                results.add(activityCenterDataDtos.get(startIndex));
            }
            activityCenterResponseDto.setActivities(results);
        }
        return activityCenterResponseDto;
    }
}
