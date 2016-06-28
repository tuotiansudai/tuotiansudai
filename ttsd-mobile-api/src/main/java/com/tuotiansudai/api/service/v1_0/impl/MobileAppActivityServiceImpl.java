package com.tuotiansudai.api.service.v1_0.impl;

import com.tuotiansudai.api.dto.v1_0.ActivityCenterDataDto;
import com.tuotiansudai.api.dto.v1_0.ActivityCenterResponseDto;
import com.tuotiansudai.api.service.v1_0.MobileAppActivityService;
import com.tuotiansudai.dto.ActivityDto;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MobileAppActivityServiceImpl implements MobileAppActivityService {

    @Autowired
    ActivityService activityService;

    @Override
    public ActivityCenterResponseDto getAppActivityCenterResponseData(String loginName, Source source, Integer index, Integer pageSize) {
        List<ActivityDto> activityDtos = activityService.getAllOperatingActivities(loginName, source);

        List<ActivityCenterDataDto> activityCenterDataDtos = new ArrayList<>();
        for (ActivityDto activityDto : activityDtos) {
            activityCenterDataDtos.add(new ActivityCenterDataDto(activityDto));
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
