package com.tuotiansudai.service;

import com.tuotiansudai.dto.ActivityDto;
import com.tuotiansudai.repository.model.ActivityModel;
import com.tuotiansudai.repository.model.ActivityStatus;
import com.tuotiansudai.repository.model.Source;

import java.util.List;

public interface ActivityService {

    List<ActivityDto> getAllOperatingActivities(String loginName, Source source);

    boolean createEditRecheckActivity(ActivityDto activityDto,ActivityStatus activityStatus,String loginName);

    ActivityModel findById(long activityId);

}
