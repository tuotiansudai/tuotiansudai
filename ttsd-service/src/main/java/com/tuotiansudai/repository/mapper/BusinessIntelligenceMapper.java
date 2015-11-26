package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.KeyValueModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BusinessIntelligenceMapper {
    List<KeyValueModel> queryUserRegisterTrendDaily(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

    List<KeyValueModel> queryUserRegisterTrendWeekly(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

    List<KeyValueModel> queryUserRegisterTrendMonthly(@Param("startTime") Date startTime, @Param("endTime") Date endTime);
}
