package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.InvestReferrerRewardModel;
import com.tuotiansudai.repository.model.ReferrerRewardStatus;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InvestReferrerRewardMapper {

    void create(InvestReferrerRewardModel model);

    InvestReferrerRewardModel findById(@Param(value = "id") long id);

    InvestReferrerRewardModel findByInvestIdAndReferrer(@Param(value = "investId") long investId,
                                                        @Param(value = "referrerLoginName") String referrerLoginName);
}
