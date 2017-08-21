package com.tuotiansudai.api.service.v1_0;

import com.tuotiansudai.activity.repository.model.ActivityCategory;
import com.tuotiansudai.api.dto.v1_0.ActivityCenterRequestDto;
import com.tuotiansudai.api.dto.v1_0.ActivityCenterResponseDto;
import com.tuotiansudai.api.dto.v1_0.ActivitySchoolSeasonStatusResponseDto;

public interface MobileAppActivityService {

    ActivityCenterResponseDto getAppActivityCenterResponseData(ActivityCenterRequestDto requestDto);

    ActivitySchoolSeasonStatusResponseDto getActivitySchoolSeasonStatusResponseDto(ActivityCategory activityCategory, String loginName);

}
