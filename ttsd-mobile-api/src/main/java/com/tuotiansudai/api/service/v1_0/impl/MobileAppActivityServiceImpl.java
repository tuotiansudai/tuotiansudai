package com.tuotiansudai.api.service.v1_0.impl;

import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.ActivityCenterDataDto;
import com.tuotiansudai.api.dto.v1_0.ActivityCenterRequestDto;
import com.tuotiansudai.api.dto.v1_0.ActivityCenterResponseDto;
import com.tuotiansudai.api.dto.v1_0.ActivityType;
import com.tuotiansudai.api.service.v1_0.MobileAppActivityService;
import com.tuotiansudai.repository.mapper.ActivityMapper;
import com.tuotiansudai.repository.model.ActivityModel;
import com.tuotiansudai.repository.model.ActivityStatus;
import com.tuotiansudai.repository.model.Source;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MobileAppActivityServiceImpl implements MobileAppActivityService {

    @Autowired
    ActivityMapper activityMapper;

    @Autowired
    @Value("${web.server}")
    private String domainName;

    @Override
    public ActivityCenterResponseDto getAppActivityCenterResponseData(ActivityCenterRequestDto requestDto) {
        Source source = Source.valueOf(requestDto.getBaseParam().getPlatform().toUpperCase());
        Integer index = requestDto.getIndex();
        Integer pageSize = requestDto.getPageSize();
        return fillActivityCenterData(requestDto.getActivityType(), index, pageSize, source);
    }

    private ActivityCenterResponseDto fillActivityCenterData(ActivityType activityType, int index, int pageSize, Source source) {
        List<ActivityCenterDataDto> activityCenterDataDtos = new ArrayList<>();
        List<ActivityModel> activityModels = Lists.newArrayList();
        if (activityType == null) {
            activityModels = activityMapper.findActiveActivities(source, new Date(), (index - 1) * pageSize, pageSize);
        } else {
            switch (activityType) {
                case CURRENT:
                    activityModels = activityMapper.findActivity(source, ActivityStatus.APPROVED, new Date(), null, (index - 1) * pageSize, pageSize);
                    break;
                case PREVIOUS:
                    activityModels = activityMapper.findActivity(source, ActivityStatus.APPROVED, null, new Date(), (index - 1) * pageSize, pageSize);
                    break;
            }
        }
        for (ActivityModel activityModel : activityModels) {
            ActivityCenterDataDto activityCenterDataDto = new ActivityCenterDataDto(activityModel);
            activityCenterDataDto.setImageUrl(domainName + "/" + activityModel.getAppPictureUrl());
            activityCenterDataDtos.add(activityCenterDataDto);
        }
        ActivityCenterResponseDto activityCenterResponseDto = new ActivityCenterResponseDto();
        activityCenterResponseDto.setIndex(index);
        activityCenterResponseDto.setPageSize(pageSize);
        activityCenterResponseDto.setTotalCount(activityMapper.countActiveActivities(source));
        activityCenterResponseDto.setActivities(activityCenterDataDtos);
        return activityCenterResponseDto;
    }
}
