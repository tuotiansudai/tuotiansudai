package com.tuotiansudai.cfca.mapper;


import com.tuotiansudai.cfca.model.AnxinContractRequestModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnxinContractRequestMapper {

    void create(AnxinContractRequestModel anxinContractRequestModel);

    void updateContractNoByInvestId(@Param(value = "contractNo") String contractNo,
                                    @Param(value = "investId") long investId,
                                    @Param(value = "txTime") String txTime);

    List<String> findBatchNoByLoanId(@Param(value = "loanId") long loanId);

}
