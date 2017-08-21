package com.tuotiansudai.activity.repository.mapper;

import com.tuotiansudai.activity.repository.model.SchoolExclusiveModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface SchoolExclusiveMapper {

    void create(SchoolExclusiveModel schoolExclusiveMobile);

    int sumJDECardByName(@Param(value = "loginName") String loginName,
                         @Param(value = "startTime") Date startTime,
                         @Param(value = "endTime") Date endTime);

    SchoolExclusiveModel findSchoolExclusiveModel(@Param(value = "loanId") long loanId,
                                                  @Param(value = "loginName") String loginName);

    void update(SchoolExclusiveModel schoolExclusiveMobile);

    int sumTopThreeIsTrue(@Param(value = "loanId") long loanId);
}
