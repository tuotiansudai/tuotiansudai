package com.tuotiansudai.service;

import com.tuotiansudai.dto.ActivityDto;
import com.tuotiansudai.repository.model.ActivityModel;
import com.tuotiansudai.repository.model.ActivityStatus;
import com.tuotiansudai.repository.model.Source;

import java.util.Date;
import java.util.List;

public interface ActivityService {

    List<ActivityDto> getAllOperatingActivities(String loginName, Source source);

    boolean saveOrUpdate(ActivityDto activityDto, ActivityStatus activityStatus, String loginName, String ip);

    ActivityDto findActivityDtoById(long activityId);

    List<ActivityDto> findAllActivities(Date startTime, Date endTime, ActivityStatus activityStatus, Source source);

}
