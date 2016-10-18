package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.PledgeEnterpriseModel;
import com.tuotiansudai.repository.model.PledgeVehicleModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PledgeEnterpriseMapper {

    void create(PledgeEnterpriseModel model);

    void update(PledgeEnterpriseModel model);

    PledgeEnterpriseModel getByLoanId(@Param(value = "loanId") long loanId);

    void deleteByLoanId(@Param(value = "loanId") long loanId);
}
