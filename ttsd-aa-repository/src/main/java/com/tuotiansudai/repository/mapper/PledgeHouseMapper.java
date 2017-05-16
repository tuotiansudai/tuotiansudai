package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.PledgeHouseModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PledgeHouseMapper {

    void create(PledgeHouseModel pledgeHouseModel);

    List<PledgeHouseModel> getByLoanId(@Param(value = "loanId") long loanId);

    void updateByLoanId(PledgeHouseModel pledgeHouseModel);

    void deleteByLoanId(long loanId);
}
