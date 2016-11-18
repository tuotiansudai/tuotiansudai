package com.tuotiansudai.activity.repository.mapper;

import com.tuotiansudai.activity.repository.model.ActivityModel;
import com.tuotiansudai.activity.repository.model.ActivityStatus;
import com.tuotiansudai.repository.model.Source;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ActivityMapper {
    void create(ActivityModel activityModel);

    void update(ActivityModel activityModel);

    ActivityModel findById(Long id);

    List<ActivityModel> findAllActivities(@Param(value = "startTime") Date startTime,
                                          @Param(value = "endTime") Date endTime,
                                          @Param(value = "activityStatus") ActivityStatus activityStatus,
                                          @Param(value = "source") Source source);

    List<ActivityModel> findActiveActivities(@Param(value = "source") Source source,
                                             @Param(value = "expiredTime") Date expiredTime,
                                             @Param(value = "index") int index,
                                             @Param(value = "pageSize") int pageSize);

    List<ActivityModel> findActivity(@Param(value = "source") Source source,
                                     @Param(value = "activityStatus") ActivityStatus activityStatus,
                                     @Param(value = "beginExpiredTime") Date beginExpiredTime,
                                     @Param(value = "endExpiredTime") Date endExpiredTime,
                                     @Param(value = "longTerm") String longTerm,
                                     @Param(value = "index") int index,
                                     @Param(value = "pageSize") int pageSize);

    int countActivity(@Param(value = "source") Source source,
                      @Param(value = "activityStatus") ActivityStatus activityStatus,
                      @Param(value = "beginExpiredTime") Date beginExpiredTime,
                      @Param(value = "endExpiredTime") Date endExpiredTime,
                      @Param(value = "longTerm") String longTerm);


}
