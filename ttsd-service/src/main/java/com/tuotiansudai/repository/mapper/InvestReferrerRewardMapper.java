package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.InvestReferrerRewardModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InvestReferrerRewardMapper {

    void create(InvestReferrerRewardModel model);

    InvestReferrerRewardModel findById(@Param(value = "id") long id);

    long findCountByInvestReferrer(@Param(value = "investId") long investId, @Param(value = "referrerLoginName") String referrerLoginName);
}
