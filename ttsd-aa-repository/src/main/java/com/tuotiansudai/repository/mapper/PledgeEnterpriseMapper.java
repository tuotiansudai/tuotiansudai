package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.PledgeEnterpriseModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PledgeEnterpriseMapper {

    void create(PledgeEnterpriseModel model);

    void update(PledgeEnterpriseModel model);

    List<PledgeEnterpriseModel> getByLoanId(@Param(value = "loanId") long loanId);

    void deleteByLoanId(@Param(value = "loanId") long loanId);
}
