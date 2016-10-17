package com.tuotiansudai.service;

import com.tuotiansudai.dto.ActivityDto;
import com.tuotiansudai.enums.Source;
import com.tuotiansudai.repository.model.ActivityStatus;

import java.util.Date;
import java.util.List;

public interface ActivityService {

    List<ActivityDto> getAllActiveActivities(String loginName, Source source);

    boolean saveOrUpdate(ActivityDto activityDto, ActivityStatus activityStatus, String loginName, String ip);

    ActivityDto findActivityDtoById(long activityId);

    List<ActivityDto> findAllActivities(Date startTime, Date endTime, ActivityStatus activityStatus, Source source);

}
