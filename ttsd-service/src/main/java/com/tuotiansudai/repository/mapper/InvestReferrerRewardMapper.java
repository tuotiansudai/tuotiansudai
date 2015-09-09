package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.InvestReferrerRewardModel;
import org.apache.ibatis.annotations.Param;

public interface InvestReferrerRewardMapper {

    void create(InvestReferrerRewardModel model);

    InvestReferrerRewardModel findById(@Param(value = "id") long id);


}
