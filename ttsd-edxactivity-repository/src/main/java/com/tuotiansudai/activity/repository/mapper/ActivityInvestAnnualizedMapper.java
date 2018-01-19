package com.tuotiansudai.activity.repository.mapper;


import com.tuotiansudai.activity.repository.model.ActivityInvestAnnualized;
import com.tuotiansudai.activity.repository.model.ActivityInvestAnnualizedModel;
import com.tuotiansudai.activity.repository.model.ActivityInvestAnnualizedView;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityInvestAnnualizedMapper {

    void create(ActivityInvestAnnualizedModel activityInvestAnnualizedModel);

    void update(ActivityInvestAnnualizedModel activityInvestAnnualizedModel);

    ActivityInvestAnnualizedModel findByActivityAndLoginName(@Param(value = "activityInvestAnnualized") ActivityInvestAnnualized activityInvestAnnualized,
                                                             @Param(value = "loginName") String loginName);

    List<ActivityInvestAnnualizedView> findByActivityAndMobile(@Param(value = "activityInvestAnnualized") ActivityInvestAnnualized activityInvestAnnualized,
                                                               @Param(value = "mobile") String mobile);
}
