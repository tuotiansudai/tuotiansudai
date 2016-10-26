package com.tuotiansudai.service;

import com.tuotiansudai.activity.repository.dto.ActivityDto;
import com.tuotiansudai.activity.repository.model.ActivityStatus;
import com.tuotiansudai.repository.model.Source;

import java.util.Date;
import java.util.List;

public interface ActivityService {

    List<ActivityDto> getAllActiveActivities(String loginName, Source source);

    boolean saveOrUpdate(ActivityDto activityDto, ActivityStatus activityStatus, String loginName, String ip);

    ActivityDto findActivityDtoById(long activityId);

    List<ActivityDto> findAllActivities(Date startTime, Date endTime, ActivityStatus activityStatus, Source source);

}
