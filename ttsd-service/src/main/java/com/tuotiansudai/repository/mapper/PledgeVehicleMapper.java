package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.PledgeVehicleModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PledgeVehicleMapper {
    void create(PledgeVehicleModel pledgeVehicleModel);

    PledgeVehicleModel getPledgeVehicleDetailByLoanId(@Param(value = "loanId") long loanId);
}
