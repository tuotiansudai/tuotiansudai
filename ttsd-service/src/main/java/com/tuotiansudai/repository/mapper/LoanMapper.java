package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.LoanModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LoanMapper {
    void create(LoanModel loanModel);

    public List<LoanModel> findLoanList(@Param(value = "status") String status,@Param(value = "loanId") String loanId,@Param(value = "loanName") String loanName,
                                        @Param(value = "startTime") String startTime,@Param(value = "endTime") String endTime,@Param(value = "currentPageNo") int currentPageNo);

    public int findLoanListCount(@Param(value = "status") String status,@Param(value = "loanId") String loanId,@Param(value = "loanName") String loanName,
                                        @Param(value = "startTime") String startTime,@Param(value = "endTime") String endTime);
    LoanModel findById(@Param(value = "loanId") long loanId);
}
