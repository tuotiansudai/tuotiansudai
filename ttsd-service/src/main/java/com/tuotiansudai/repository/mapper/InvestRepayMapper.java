package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.InvestRepayModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvestRepayMapper {

    void create(List<InvestRepayModel> investRepayModels);

    List<InvestRepayModel> findByInvestId(long investId);

    InvestRepayModel findByInvestIdAndPeriod(@Param(value = "investId") long investId, @Param(value = "period") int period);

    List<InvestRepayModel> findCompletedInvestRepayByIdAndStatus(@Param(value = "investId") long investId);

    void update(InvestRepayModel investRepayModel);
}
