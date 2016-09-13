package com.tuotiansudai.api.service.v1_0.impl;

import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.ActivityCenterDataDto;
import com.tuotiansudai.api.dto.v1_0.ActivityCenterRequestDto;
import com.tuotiansudai.api.dto.v1_0.ActivityCenterResponseDto;
import com.tuotiansudai.api.dto.v1_0.ActivityCenterType;
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

    @Value("${web.server}")
    private String domainName;

    @Override
    public ActivityCenterResponseDto getAppActivityCenterResponseData(ActivityCenterRequestDto requestDto) {
        Source source = Source.valueOf(requestDto.getBaseParam().getPlatform().toUpperCase());
        Integer index = requestDto.getIndex();
        Integer pageSize = requestDto.getPageSize();
        if (null == index) {
            index = 1;
        }
        if (null == pageSize) {
            pageSize = 10;
        }
        return fillActivityCenterData(requestDto.getActivityType() == null?ActivityCenterType.CURRENT:requestDto.getActivityType(), index, pageSize, source);
    }

    private ActivityCenterResponseDto fillActivityCenterData(ActivityCenterType activityType, int index, int pageSize, Source source) {
        List<ActivityCenterDataDto> activityCenterDataDtos = new ArrayList<>();
        List<ActivityModel> activityModels = Lists.newArrayList();
        int totalCount = 0 ;

        switch (activityType) {
            case CURRENT:
                activityModels = activityMapper.findActivity(source, ActivityStatus.APPROVED, new Date(), null, "true", (index - 1) * pageSize, pageSize);
                totalCount = activityMapper.countActivity(source, ActivityStatus.APPROVED, new Date(), null, "true");
                break;
            case PREVIOUS:
                activityModels = activityMapper.findActivity(source, ActivityStatus.APPROVED, null, new Date(), "false", (index - 1) * pageSize, pageSize);
                totalCount = activityMapper.countActivity(source, ActivityStatus.APPROVED, new Date(), null, "false");
                break;
        }
        for (ActivityModel activityModel : activityModels) {
            ActivityCenterDataDto activityCenterDataDto = new ActivityCenterDataDto(activityModel);
            activityCenterDataDto.setImageUrl(domainName + "/" + activityModel.getAppPictureUrl());
            activityCenterDataDtos.add(activityCenterDataDto);
        }
        ActivityCenterResponseDto activityCenterResponseDto = new ActivityCenterResponseDto();
        activityCenterResponseDto.setIndex(index);
        activityCenterResponseDto.setPageSize(pageSize);
        activityCenterResponseDto.setTotalCount(totalCount);
        activityCenterResponseDto.setActivities(activityCenterDataDtos);
        return activityCenterResponseDto;
    }
}
