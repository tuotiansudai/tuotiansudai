package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.LoanRiskManagementTitleRelationModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRiskManagementTitleRelationMapper {

    void create(@Param("loanTitleModels") List<LoanRiskManagementTitleRelationModel> loanRiskManagementTitleRelationModels);

    void updateLoanIdByLoanApplicationId(@Param("loanId") long loanId, @Param("loanApplicationId") long loanApplicationId);

    List<LoanRiskManagementTitleRelationModel> findByLoanApplicationId(@Param("loanApplicationId") long loanApplicationId);

    LoanRiskManagementTitleRelationModel findByLoanApplicationIdAndTitleId(@Param("loanApplicationId") long loanApplicationId,
                                                                           @Param("titleId") long titleId);

    List<String> findTitleNameByLoanId(@Param("loanId") long loanId);

    void deleteByLoanApplication(@Param("loanApplicationId") long loanApplicationId);
}
