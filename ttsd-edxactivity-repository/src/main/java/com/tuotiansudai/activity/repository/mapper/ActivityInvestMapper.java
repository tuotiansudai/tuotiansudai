package com.tuotiansudai.activity.repository.mapper;

import com.tuotiansudai.activity.repository.model.ActivityInvestModel;
import com.tuotiansudai.activity.repository.model.ActivityInvestView;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ActivityInvestMapper {

    void create(ActivityInvestModel activityInvestModel);

    List<ActivityInvestView> sumAmountByNameDateAndActivity(@Param(value = "activityName") String activityName,
                                                            @Param(value = "startTime") Date startTime,
                                                            @Param(value = "endTime") Date endTime);

    List<ActivityInvestModel> findAllByActivityLoginNameAndTime(@Param(value = "loginName") String loginName,
                                                                @Param(value = "activityName") String activityName,
                                                                @Param(value = "startTime") Date startTime,
                                                                @Param(value = "endTime") Date endTime);

    long sumInvestAmountByActivityLoginNameAndTime(@Param(value = "loginName") String loginName,
                                                   @Param(value = "activityName") String activityName,
                                                   @Param(value = "startTime") Date startTime,
                                                   @Param(value = "endTime") Date endTime);

    List<ActivityInvestModel> findByLoanId(@Param(value = "loanId") long loanId);
}
