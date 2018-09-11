package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.InvestExtraRateModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvestExtraRateMapper {

    List<InvestExtraRateModel> findByLoanId(@Param(value = "loanId") long loanId);

    void update(InvestExtraRateModel investExtraRateModel);

    void create(InvestExtraRateModel investExtraRateModel);

    InvestExtraRateModel findByInvestId(@Param(value = "investId") long investId);

    InvestExtraRateModel findById(@Param(value = "id") long id);

}
