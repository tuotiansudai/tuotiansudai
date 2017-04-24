package com.tuotiansudai.repository.mapper;


import com.tuotiansudai.repository.model.LoanerEnterpriseInfoModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanerEnterpriseInfoMapper {

    void create(LoanerEnterpriseInfoModel model);

    LoanerEnterpriseInfoModel getByLoanId(@Param(value = "loanId") long loanId);

    void deleteByLoanId(@Param(value = "loanId") long loanId);
}
