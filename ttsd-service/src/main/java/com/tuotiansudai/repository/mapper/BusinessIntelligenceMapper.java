package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.dto.Granularity;
import com.tuotiansudai.dto.UserStage;
import com.tuotiansudai.repository.model.KeyValueModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BusinessIntelligenceMapper {
    List<KeyValueModel> queryUserRegisterTrend(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("granularity") Granularity granularity, @Param("province") String province, @Param("userStage")UserStage userStage);

    List<KeyValueModel> queryUserRechargeTrend(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("granularity") Granularity granularity, @Param("province") String province);

    List<KeyValueModel> queryUserWithdrawTrend(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("granularity") Granularity granularity, @Param("province") String province);

    List<KeyValueModel> queryUserAccountTrend(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("granularity") Granularity granularity, @Param("province") String province);

    List<KeyValueModel> queryInvestViscosity(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("province") String province);
}
