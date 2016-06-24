package com.tuotiansudai.service;

import com.tuotiansudai.dto.ActivityDto;

import java.util.List;

public interface ActivityService {

    List<ActivityDto> getAllOperatingActivities(String loginName);

}
