package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.InvestRepayModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvestRepayMapper {

    void create(List<InvestRepayModel> investRepayModels);

    List<InvestRepayModel> findByInvestId(long investId);

    List<InvestRepayModel> findByLoginNameAndInvestId(@Param(value = "loginName") String loginName, @Param(value = "investId") long investId);

    InvestRepayModel findByInvestIdAndPeriod(@Param(value = "investId") long investId, @Param(value = "period") int period);

    void update(InvestRepayModel investRepayModel);
}
