package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.PledgeHouseModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PledgeHouseMapper {

    void create(PledgeHouseModel pledgeHouseModel);

    PledgeHouseModel getByLoanId(@Param(value = "loanId") long loanId);

    void updateByLoanId(PledgeHouseModel pledgeHouseModel);

    void deleteByLoanId(long loanId);
}
