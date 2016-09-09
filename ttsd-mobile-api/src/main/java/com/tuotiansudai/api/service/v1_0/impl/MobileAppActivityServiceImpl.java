package com.tuotiansudai.api.service.v1_0.impl;

import com.tuotiansudai.api.dto.v1_0.ActivityCenterDataDto;
import com.tuotiansudai.api.dto.v1_0.ActivityCenterResponseDto;
import com.tuotiansudai.api.service.v1_0.MobileAppActivityService;
import com.tuotiansudai.repository.mapper.ActivityMapper;
import com.tuotiansudai.repository.model.ActivityModel;
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
    public ActivityCenterResponseDto getAppActivityCenterResponseData(String loginName, Source source, Integer index, Integer pageSize) {
        if (null == index) {
            index = 1;
        }
        if (null == pageSize) {
            pageSize = 10;
        }

        List<ActivityModel> activityModels = activityMapper.findMobileActiveActivities(source, new Date(), (index - 1) * pageSize, pageSize);

        List<ActivityCenterDataDto> activityCenterDataDtos = new ArrayList<>();
        for (ActivityModel activityModel : activityModels) {
            ActivityCenterDataDto activityCenterDataDto = new ActivityCenterDataDto(activityModel);
            activityCenterDataDto.setImageUrl(domainName + "/" + activityModel.getAppPictureUrl());
            activityCenterDataDtos.add(activityCenterDataDto);
        }

        ActivityCenterResponseDto activityCenterResponseDto = new ActivityCenterResponseDto();
        activityCenterResponseDto.setIndex(index);
        activityCenterResponseDto.setPageSize(pageSize);
        activityCenterResponseDto.setTotalCount(activityMapper.countMobileActiveActivities(source));
        activityCenterResponseDto.setActivities(activityCenterDataDtos);

        return activityCenterResponseDto;
    }
}
