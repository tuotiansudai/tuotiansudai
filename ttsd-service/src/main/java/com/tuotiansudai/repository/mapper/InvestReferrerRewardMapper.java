package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.InvestReferrerRewardModel;
import com.tuotiansudai.repository.model.ReferrerRewardStatus;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvestReferrerRewardMapper {

    void create(InvestReferrerRewardModel model);

    InvestReferrerRewardModel findById(@Param(value = "id") long id);

    List<InvestReferrerRewardModel> findByInvestId(@Param(value="InvestId") long investId);

    InvestReferrerRewardModel findByInvestIdAndReferrer(@Param(value = "investId") long investId,
                                                        @Param(value = "referrerLoginName") String referrerLoginName);
}
