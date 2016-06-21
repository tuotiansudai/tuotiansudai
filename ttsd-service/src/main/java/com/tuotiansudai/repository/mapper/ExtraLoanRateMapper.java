package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.ExtraLoanRateModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExtraLoanRateMapper {

    void create(List<ExtraLoanRateModel> extraLoanRateModels);

    void deleteByLoanId(@Param(value = "loanId") long loanId);

    List<ExtraLoanRateModel> findByLoanId(@Param(value = "loanId") long loanId);

    double findMaxRateByLoanId(@Param(value = "loanId") long loanId);

    List<ExtraLoanRateModel> findByLoanIdOrderByRate(@Param(value = "loanId") long loanId);

}
