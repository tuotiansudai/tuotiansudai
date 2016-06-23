package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.ActivityModel;
import com.tuotiansudai.repository.model.ActivityStatus;
import com.tuotiansudai.repository.model.Source;

import java.util.Date;
import java.util.List;

public interface ActivityMapper {
    void create(ActivityModel activityModel);

    void update(ActivityModel activityModel);

    ActivityModel findById(long id);

    List<ActivityModel> findActivities(Date ActivatedStartTime, Date ActivatedEndTime, ActivityStatus activityStatus, Source source);
}
