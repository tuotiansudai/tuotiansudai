package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.LoanRiskManagementTitleRelationModel;
import com.tuotiansudai.repository.model.LoanTitleRelationModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRiskManagementTitleRelationMapper {

    void create(@Param("loanTitleModels") List<LoanRiskManagementTitleRelationModel> loanRiskManagementTitleRelationModels);

    void updateLoanIdByLoanApplicationId(@Param("loanId") long loanId, @Param("loanApplicationId") long loanApplicationId);

    List<LoanRiskManagementTitleRelationModel> findByLoanApplicationId(@Param("loanApplicationId") long loanApplicationId);

    List<String> findTitleNameByLoanId(@Param("loanId") long loanId);

    void deleteByLoanApplication(@Param("loanApplicationId") long loanApplicationId);
}
