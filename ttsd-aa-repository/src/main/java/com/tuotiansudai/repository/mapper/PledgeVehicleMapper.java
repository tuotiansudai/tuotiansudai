package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.PledgeVehicleModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PledgeVehicleMapper {
    void create(PledgeVehicleModel pledgeVehicleModel);

    List<PledgeVehicleModel> getByLoanId(@Param(value = "loanId") long loanId);

    void updateByLoanId(PledgeVehicleModel pledgeVehicleModel);

    void deleteByLoanId(long loanId);
}
