package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.repository.model.LoanStatus;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
@Repository
public interface LoanMapper {
    void create(LoanModel loanModel);

    LoanModel findById(@Param(value = "loanId") long loanId);

    void update(LoanModel loanModel);

    List<LoanModel> findByStatus(@Param(value = "status") LoanStatus status);

    void updateStatus(@Param(value = "loanId") long loanId, @Param(value = "status") LoanStatus status);

    public List<LoanModel> findLoanList(@Param(value = "status") LoanStatus status,@Param(value = "loanId") long loanId,@Param(value = "loanName") String loanName,
                                        @Param(value = "startTime") String startTime,@Param(value = "endTime") String endTime,@Param(value = "currentPageNo") int currentPageNo);

    public int findLoanListCount(@Param(value = "status") LoanStatus status,@Param(value = "loanId") long loanId,@Param(value = "loanName") String loanName,
                                 @Param(value = "startTime") String startTime,@Param(value = "endTime") String endTime);

    List<LoanModel> findRepayingPaginationByLoanerLoginName(@Param(value = "loanerLoginName") String loanerLoginName,
                                                            @Param(value = "index") int index,
                                                            @Param(value = "pageSize") int pageSize,
                                                            @Param(value = "startDate") Date startDate,
                                                            @Param(value = "endDate") Date endDate);

    List<LoanModel> findCompletedPaginationByLoanerLoginName(@Param(value = "loanerLoginName") String loanerLoginName,
                                                             @Param(value = "index") int index,
                                                             @Param(value = "pageSize") int pageSize,
                                                             @Param(value = "startDate") Date startDate,
                                                             @Param(value = "endDate") Date endDate);

    List<LoanModel> findCanceledPaginationByLoanerLoginName(@Param(value = "loanerLoginName") String loanerLoginName,
                                                            @Param(value = "index") int index,
                                                            @Param(value = "pageSize") int pageSize,
                                                            @Param(value = "startDate") Date startDate,
                                                            @Param(value = "endDate") Date endDate);

    long findCountRepayingByLoanerLoginName(@Param(value = "loanerLoginName") String loanerLoginName,
                                            @Param(value = "startDate") Date startDate,
                                            @Param(value = "endDate") Date endDate);

    long findCountCompletedByLoanerLoginName(@Param(value = "loanerLoginName") String loanerLoginName,
                                             @Param(value = "startDate") Date startDate,
                                             @Param(value = "endDate") Date endDate);

    long findCountCanceledByLoanerLoginName(@Param(value = "loanerLoginName") String loanerLoginName,
                                            @Param(value = "startDate") Date startDate,
                                            @Param(value = "endDate") Date endDate);

}
