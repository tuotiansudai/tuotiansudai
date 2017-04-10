package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.enums.riskestimation.*;
import com.tuotiansudai.repository.model.RiskEstimateModel;
import com.tuotiansudai.repository.model.RiskEstimateViewModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RiskEstimateMapper {

    int create(RiskEstimateModel model);

    int update(RiskEstimateModel model);

    RiskEstimateModel findByLoginName(@Param(value = "loginName") String loginName);

    long countByConsole(@Param(value = "estimate") Estimate estimate,
                        @Param(value = "income") Income income,
                        @Param(value = "rate") Rate rate,
                        @Param(value = "duration") Duration duration,
                        @Param(value = "age") Age age);

    List<RiskEstimateViewModel> findByConsole(@Param(value = "estimate") Estimate estimate,
                                              @Param(value = "income") Income income,
                                              @Param(value = "rate") Rate rate,
                                              @Param(value = "duration") Duration duration,
                                              @Param(value = "age") Age age,
                                              @Param(value = "offset") int offset);

}
