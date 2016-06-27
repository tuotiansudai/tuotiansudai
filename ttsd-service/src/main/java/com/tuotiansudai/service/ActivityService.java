package com.tuotiansudai.service;

import com.tuotiansudai.dto.ActivityDto;
import com.tuotiansudai.repository.model.ActivityStatus;

import java.util.List;

public interface ActivityService {

    List<ActivityDto> getAllOperatingActivities(String loginName);

    boolean createEditRecheckActivity(ActivityDto activityDto,ActivityStatus activityStatus,String loginName);

}
