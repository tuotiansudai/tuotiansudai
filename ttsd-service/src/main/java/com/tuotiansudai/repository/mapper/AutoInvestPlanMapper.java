package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.AutoInvestPlanModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface AutoInvestPlanMapper {

    void create(AutoInvestPlanModel autoInvestPlanModel);

    void update(AutoInvestPlanModel autoInvestPlanModel);

    void enable(@Param("loginName") String loginName);

    void disable(@Param("loginName") String loginName);

    AutoInvestPlanModel findByLoginName(@Param("loginName") String loginName);

    List<AutoInvestPlanModel> findEnabledPlanByPeriod(@Param("period") int period,
                                                       @Param("endTime") Date endTime);
}
