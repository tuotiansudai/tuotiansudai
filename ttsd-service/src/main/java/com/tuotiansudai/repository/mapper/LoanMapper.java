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
                                        @Param(value = "startTime") Date startTime,@Param(value = "endTime") Date endTime,@Param(value = "currentPageNo") int currentPageNo,int pageSize);

    public int findLoanListCount(@Param(value = "status") LoanStatus status,@Param(value = "loanId") long loanId,@Param(value = "loanName") String loanName,
                                 @Param(value = "startTime") Date startTime,@Param(value = "endTime") Date endTime);

    List<LoanModel> findRepayingPaginationByLoanerLoginName(@Param(value = "loanerLoginName") String loanerLoginName,
                                                            @Param(value = "index") int index,
                                                            @Param(value = "pageSize") int pageSize,
                                                            @Param(value = "startTime") Date startTime,
                                                            @Param(value = "endTime") Date endTime);

    List<LoanModel> findCompletedPaginationByLoanerLoginName(@Param(value = "loanerLoginName") String loanerLoginName,
                                                             @Param(value = "index") int index,
                                                             @Param(value = "pageSize") int pageSize,
                                                             @Param(value = "startTime") Date startTime,
                                                             @Param(value = "endTime") Date endTime);

    List<LoanModel> findCanceledPaginationByLoanerLoginName(@Param(value = "loanerLoginName") String loanerLoginName,
                                                            @Param(value = "index") int index,
                                                            @Param(value = "pageSize") int pageSize,
                                                            @Param(value = "startTime") Date startTime,
                                                            @Param(value = "endTime") Date endTime);

    long findCountRepayingByLoanerLoginName(@Param(value = "loanerLoginName") String loanerLoginName,
                                            @Param(value = "startTime") Date startTime,
                                            @Param(value = "endTime") Date endTime);

    long findCountCompletedByLoanerLoginName(@Param(value = "loanerLoginName") String loanerLoginName,
                                             @Param(value = "startTime") Date startTime,
                                             @Param(value = "endTime") Date endTime);

    long findCountCanceledByLoanerLoginName(@Param(value = "loanerLoginName") String loanerLoginName,
                                            @Param(value = "startTime") Date startTime,
                                            @Param(value = "endTime") Date endTime);

}
