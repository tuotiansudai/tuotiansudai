package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.LoanRiskManagementTitleRelationModel;
import com.tuotiansudai.repository.model.LoanTitleRelationModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRiskManagementTitleRelationMapper {

    void create(@Param("loanTitleModels") List<LoanRiskManagementTitleRelationModel> loanRiskManagementTitleRelationModels);

    List<LoanRiskManagementTitleRelationModel> findByLoanId(@Param("loanId") long loanId);

    List<LoanRiskManagementTitleRelationModel> findByLoanApplicationId(@Param("loanApplicationId") long loanApplicationId);

    void delete(@Param("loanId") long loanId);
}
