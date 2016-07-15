package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.AbstractPledgeDetail;
import com.tuotiansudai.repository.model.PledgeHouseModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PledgeHouseMapper {
    void create(PledgeHouseModel pledgeHouseModel);

    AbstractPledgeDetail getPledgeHouseDetailByLoanId(@Param(value = "loanId") long loanId);
}
