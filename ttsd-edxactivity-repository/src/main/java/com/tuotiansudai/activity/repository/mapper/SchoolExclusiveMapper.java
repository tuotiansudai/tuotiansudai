package com.tuotiansudai.activity.repository.mapper;

import com.tuotiansudai.activity.repository.dto.SchoolExclusiveDto;
import com.tuotiansudai.activity.repository.model.SchoolExclusiveModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface SchoolExclusiveMapper {

    void create(SchoolExclusiveModel schoolExclusiveMobile);

    List<SchoolExclusiveDto> sumJDECardByName(@Param(value = "startTime") Date startTime,
                                              @Param(value = "endTime") Date endTime);

    SchoolExclusiveModel findSchoolExclusiveModel(@Param(value = "investId") long investId,
                                                  @Param(value = "loginName") String loginName);

    void update(SchoolExclusiveModel schoolExclusiveMobile);

    int sumTopThreeIsTrue(@Param(value = "investId") long investId);
}
