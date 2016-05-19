package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface LoanMapper {

    void create(LoanModel loanModel);

    LoanModel findById(@Param(value = "loanId") long loanId);

    List<LoanModel> findLoanListWeb(@Param(value = "productType") ProductType productType,
                                    @Param(value = "status") LoanStatus status,
                                    @Param(value = "rateStart") double rateStart,
                                    @Param(value = "rateEnd") double rateEnd,
                                    @Param(value = "index") int index);

    int findLoanListCountWeb(@Param(value = "productType") ProductType productType,
                             @Param(value = "status") LoanStatus status,
                             @Param(value = "rateStart") double rateStart,
                             @Param(value = "rateEnd") double rateEnd);

    void update(LoanModel loanModel);

    List<LoanModel> findByStatus(@Param(value = "status") LoanStatus status);

    void updateStatus(@Param(value = "loanId") long loanId, @Param(value = "status") LoanStatus status);

    List<LoanModel> findRepayingPaginationByAgentLoginName(@Param(value = "agentLoginName") String agentLoginName,
                                                           @Param(value = "index") int index,
                                                           @Param(value = "pageSize") int pageSize,
                                                           @Param(value = "startTime") Date startTime,
                                                           @Param(value = "endTime") Date endTime);

    List<LoanModel> findCompletedPaginationByAgentLoginName(@Param(value = "agentLoginName") String agentLoginName,
                                                            @Param(value = "index") int index,
                                                            @Param(value = "pageSize") int pageSize,
                                                            @Param(value = "startTime") Date startTime,
                                                            @Param(value = "endTime") Date endTime);

    List<LoanModel> findCanceledPaginationByAgentLoginName(@Param(value = "agentLoginName") String agentLoginName,
                                                           @Param(value = "index") int index,
                                                           @Param(value = "pageSize") int pageSize,
                                                           @Param(value = "startTime") Date startTime,
                                                           @Param(value = "endTime") Date endTime);

    long findCountRepayingByAgentLoginName(@Param(value = "agentLoginName") String agentLoginName,
                                           @Param(value = "startTime") Date startTime,
                                           @Param(value = "endTime") Date endTime);

    long findCountCompletedByAgentLoginName(@Param(value = "agentLoginName") String agentLoginName,
                                            @Param(value = "startTime") Date startTime,
                                            @Param(value = "endTime") Date endTime);

    long findCountCanceledByAgentLoginName(@Param(value = "agentLoginName") String agentLoginName,
                                           @Param(value = "startTime") Date startTime,
                                           @Param(value = "endTime") Date endTime);

    List<LoanModel> findLoanList(@Param(value = "status") LoanStatus status, @Param(value = "loanId") Long loanId, @Param(value = "loanName") String loanName,
                                 @Param(value = "startTime") Date startTime, @Param(value = "endTime") Date endTime, @Param(value = "currentPageNo") int currentPageNo, @Param(value = "pageSize") int pageSize);

    int findLoanListCount(@Param(value = "status") LoanStatus status, @Param(value = "loanId") Long loanId, @Param(value = "loanName") String loanName,
                          @Param(value = "startTime") Date startTime, @Param(value = "endTime") Date endTime);

    void updateRaisingCompleteTime(@Param(value = "loanId") long loanId,
                                   @Param(value = "raisingCompleteTime") Date raisingCompleteTime);

    List<LoanModel> findHomeLoan();

    List<LoanAchievementView> findLoanAchievement(@Param(value = "index") int index, @Param(value = "pageSize") int pageSize, @Param(value = "loginName") String loginName);

    long findLoanAchievementCount(@Param(value = "loginName") String loginName);

}
